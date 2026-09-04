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
        // Defensive check in case Gemini returns no usable body
        if (response == null) {
            throw new RuntimeException("Gemini returned an empty response");
        }

        // Prefer the simple convenience field if Gemini provides it
        if (response.outputText() != null && !response.outputText().isBlank()) {
            return response.outputText();
        }

        // Fall back to reading from steps if output_text is not present
        if (response.steps() == null) {
            throw new RuntimeException("No text output found in Gemini response");
        }

        return response.steps()
                .stream()
                .filter(step -> "model_output".equals(step.type()))
                .filter(step -> step.content() != null)
                .flatMap(step -> step.content().stream())
                .filter(content -> "text".equals(content.type()))
                .map(GeminiContent::text)
                .filter(text -> text != null && !text.isBlank())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No text output found in Gemini response"));
    }
}
