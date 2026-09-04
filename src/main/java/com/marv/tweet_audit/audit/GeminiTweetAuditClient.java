package com.marv.tweet_audit.audit;

import com.marv.tweet_audit.config.GeminiProperties;
import com.marv.tweet_audit.gemini.GeminiContent;
import com.marv.tweet_audit.gemini.GeminiInteractionRequest;
import com.marv.tweet_audit.gemini.GeminiInteractionResponse;
import com.marv.tweet_audit.gemini.GeminiResponseFormat;
import com.marv.tweet_audit.model.AuditCriteria;
import com.marv.tweet_audit.model.AuditDecision;
import com.marv.tweet_audit.model.Tweet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GeminiTweetAuditClient implements TweetAuditClient{

    private final GeminiProperties geminiProperties;
    private final AuditCriteria auditCriteria;

    private String buildPrompt(Tweet tweet) {
        return """
            You are auditing old tweets for alignment with the user's current values.

            Criteria:
            - Forbidden words: %s
            - Professional check enabled: %s
            - Desired tone: %s
            - Exclude politics: %s

            Tweet:
            "%s"

            Decide whether this tweet should be flagged for possible deletion.

            Return JSON only in this exact format:
            {
              "flagged": true,
              "reason": "short reason here"
            }
            """.formatted(
                auditCriteria.getForbiddenWords(),
                auditCriteria.isProfessionalCheck(),
                auditCriteria.getTone(),
                auditCriteria.isExcludePolitics(),
                tweet.text()
        );
    }

    @Override
    public AuditDecision audit(Tweet tweet) {


        // 1. Build a Gemini prompt
        // 2. Send the tweet + criteria to Gemini
        // 3. Parse Gemini's response
        // 4. Return AuditDecision

        String prompt = buildPrompt(tweet);

// Temporary: print prompt so we can inspect it before API call
        System.out.println(prompt);

        return new AuditDecision(false, "Gemini not connected yet");
//        throw new UnsupportedOperationException("Gemini client not implemented yet");
    }


    private GeminiInteractionRequest buildRequest(Tweet tweet) {

        // Build the actual instruction we want Gemini to follow
        String prompt = buildPrompt(tweet);

        // This schema tells Gemini the exact JSON shape we expect back
        Map<String, Object> schema = Map.of(
                "type", "object",
                "properties", Map.of(
                        "flagged", Map.of("type", "boolean"),
                        "reason", Map.of("type", "string")
                ),
                "required", List.of("flagged", "reason")
        );

        // response_format asks Gemini to return JSON text matching the schema
        GeminiResponseFormat responseFormat = new GeminiResponseFormat(
                "text",
                "application/json",
                schema
        );

        // This is the full request body we will send to Gemini
        return new GeminiInteractionRequest(
                geminiProperties.getModel(),
                prompt,
                responseFormat
        );
    }


    private String extractOutputText(GeminiInteractionResponse response) {
        // Gemini may return a failed or empty response
        if (response == null || response.steps() == null) {
            throw new RuntimeException("Gemini returned an empty response");
        }

        return response.steps()
                .stream()

                // We only want the model's final visible output
                .filter(step -> "model_output".equals(step.type()))

                // Some steps may not have content
                .filter(step -> step.content() != null)

                // Flatten List<GeminiContent> from all model_output steps
                .flatMap(step -> step.content().stream())

                // We only want text content
                .filter(content -> "text".equals(content.type()))

                // Extract the actual text
                .map(GeminiContent::text)

                // Ignore null/blank text
                .filter(text -> text != null && !text.isBlank())

                // Take the first valid model output text
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No text output found in Gemini response"));
    }
}
