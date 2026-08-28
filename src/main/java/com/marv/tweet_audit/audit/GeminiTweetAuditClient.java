package com.marv.tweet_audit.audit;

import com.marv.tweet_audit.config.GeminiProperties;
import com.marv.tweet_audit.model.AuditCriteria;
import com.marv.tweet_audit.model.AuditDecision;
import com.marv.tweet_audit.model.Tweet;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GeminiTweetAuditClient implements TweetAuditClient{

    private final GeminiProperties geminiProperties;
    private final AuditCriteria auditCriteria;

    @Override
    public AuditDecision audit(Tweet tweet) {

        // 1. Build a Gemini prompt
        // 2. Send the tweet + criteria to Gemini
        // 3. Parse Gemini's response
        // 4. Return AuditDecision

        throw new UnsupportedOperationException("Gemini client not implemented yet");
    }
}
