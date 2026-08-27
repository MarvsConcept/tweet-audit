package com.marv.tweet_audit.audit;

import com.marv.tweet_audit.model.AuditCriteria;
import com.marv.tweet_audit.model.AuditDecision;
import com.marv.tweet_audit.model.Tweet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FakeTweetAuditClientTest {

    @Test
    void shouldFlagTweetContainingForbiddenWord() {

        // Arrange: Create criteria manually for this unit test
        AuditCriteria criteria = new AuditCriteria();
        criteria.setForbiddenWords(List.of("crypto", "NFT"));

        FakeTweetAuditClient client = new FakeTweetAuditClient(criteria);

        Tweet tweet = new Tweet(
                "123",
                "I made money from NFT trading",
                "date"
        );

        // Act: audit the tweet
        AuditDecision decision = client.audit(tweet);

        // Assert: tweet should be flagged
        assertThat(decision.flagged()).isTrue();
        assertThat(decision.reason()).isEqualTo("Contains forbidden word");
    }

    @Test
    void shouldNotFlagTweetWithoutForbiddenWord() {

        // Arrange
        AuditCriteria criteria = new AuditCriteria();
        criteria.setForbiddenWords(List.of("crypto", "NFT"));

        FakeTweetAuditClient client = new FakeTweetAuditClient(criteria);

        Tweet tweet = new Tweet(
                "456",
                "Learning backend engineering today",
                "date"
        );

        // Act
        AuditDecision decision = client.audit(tweet);

        // Assert
        assertThat(decision.flagged()).isFalse();
        assertThat(decision.reason()).isEqualTo("Tweet is aligned");
    }
}
