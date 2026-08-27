package com.marv.tweet_audit.audit;

import com.marv.tweet_audit.model.AuditCriteria;
import com.marv.tweet_audit.model.AuditDecision;
import com.marv.tweet_audit.model.Tweet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class FakeTweetAuditClient implements TweetAuditClient{

    private final AuditCriteria auditCriteria;

    @Override
    public AuditDecision audit(Tweet tweet) {

        String text = tweet.text().toLowerCase();

        boolean containsForbiddenWord =
                auditCriteria.getForbiddenWords()
                        .stream()
                        .anyMatch(word -> text.contains(word.toLowerCase()));

        if (containsForbiddenWord) {
            return new AuditDecision(true, "Contains forbidden word");
        }

        return new AuditDecision(false, "Tweet is aligned");
    }

}
