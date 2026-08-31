package com.marv.tweet_audit.audit;

import com.marv.tweet_audit.config.GeminiProperties;
import com.marv.tweet_audit.model.AuditCriteria;
import com.marv.tweet_audit.model.AuditDecision;
import com.marv.tweet_audit.model.Tweet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
}
