package com.marv.tweet_audit.audit;

import com.marv.tweet_audit.model.AuditDecision;
import com.marv.tweet_audit.model.Tweet;
import org.springframework.stereotype.Component;

@Component
public class FakeTweetAuditClient implements TweetAuditClient{

    @Override
    public AuditDecision audit(Tweet tweet) {

        String text = tweet.text().toLowerCase();

        if (text.contains("crypto") || text.contains("nft") || text.contains("hustle")) {
            return new AuditDecision(true, "Contains forbidden word");
        }

        return new AuditDecision(false, "Tweet is aligned");
    }

}
