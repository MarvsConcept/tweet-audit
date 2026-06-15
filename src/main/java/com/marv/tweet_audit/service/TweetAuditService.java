package com.marv.tweet_audit.service;

import com.marv.tweet_audit.audit.TweetAuditClient;
import com.marv.tweet_audit.model.AuditDecision;
import com.marv.tweet_audit.model.Tweet;
import com.marv.tweet_audit.url.TweetUrlBuilder;
import com.marv.tweet_audit.writer.CsvReportWriter;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
public class TweetAuditService {

    private final TweetUrlBuilder tweetUrlBuilder;
    private final CsvReportWriter csvReportWriter;
    private final TweetAuditClient tweetAuditClient;

    public void audit(List<Tweet> tweets, String username, Path outputPath) {

        for (Tweet tweet : tweets) {
            //
            AuditDecision decision = tweetAuditClient.audit(tweet);

            if (decision.flagged()) {
                // Builds a tweetLink from the username and tweetId
                String tweetUrl = tweetUrlBuilder.build(username, tweet.id());
                // Writes the tweetLink into a Csv file.
                csvReportWriter.writeFlaggedTweet(outputPath, tweetUrl);
            }
        }
    }
}
