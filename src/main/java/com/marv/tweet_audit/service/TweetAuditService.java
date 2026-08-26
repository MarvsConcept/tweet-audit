package com.marv.tweet_audit.service;

import com.marv.tweet_audit.audit.TweetAuditClient;
import com.marv.tweet_audit.checkpoint.CheckpointService;
import com.marv.tweet_audit.model.AuditDecision;
import com.marv.tweet_audit.model.Tweet;
import com.marv.tweet_audit.url.TweetUrlBuilder;
import com.marv.tweet_audit.writer.CsvReportWriter;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Builder
public class TweetAuditService {

    private final TweetUrlBuilder tweetUrlBuilder;
    private final CsvReportWriter csvReportWriter;
    private final TweetAuditClient tweetAuditClient;
    private final CheckpointService checkpointService;

    public void audit(List<Tweet> tweets, String username, Path outputPath, Path checkpointPath) {

        Set<String> processedTweetIds =
                checkpointService.loadProcessedTweetIds(checkpointPath);

        for (Tweet tweet : tweets) {

            // Load the processed Id's before auditing the tweets
            if (processedTweetIds.contains(tweet.id())) {
                continue;
            }

            AuditDecision decision = tweetAuditClient.audit(tweet);

            if (decision.flagged()) {
                // Builds a tweetLink from the username and tweetId
                String tweetUrl = tweetUrlBuilder.build(username, tweet.id());
                // Writes the tweetLink into a Csv file.
                csvReportWriter.writeFlaggedTweet(outputPath, tweetUrl);
            }

            // mark each tweet after processing
            checkpointService.markProcessed(checkpointPath, tweet.id());
        }
    }
}
