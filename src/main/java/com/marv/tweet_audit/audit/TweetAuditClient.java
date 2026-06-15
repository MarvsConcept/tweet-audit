package com.marv.tweet_audit.audit;

import com.marv.tweet_audit.model.AuditDecision;
import com.marv.tweet_audit.model.Tweet;

public interface TweetAuditClient {

    AuditDecision audit(Tweet tweet);
}
