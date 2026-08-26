package com.marv.tweet_audit.service;

import com.marv.tweet_audit.audit.TweetAuditClient;
import com.marv.tweet_audit.checkpoint.CheckpointService;
import com.marv.tweet_audit.model.AuditDecision;
import com.marv.tweet_audit.model.Tweet;
import com.marv.tweet_audit.url.TweetUrlBuilder;
import com.marv.tweet_audit.writer.CsvReportWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TweetAuditServiceTest {

    @TempDir
    Path tempDir;

    @Mock
    private TweetUrlBuilder tweetUrlBuilder;
    @Mock
    private CsvReportWriter csvReportWriter;
    @Mock
    private TweetAuditClient tweetAuditClient;
    @Mock
    private CheckpointService checkpointService;

    @Test
    void shouldOnlyWriteFlaggedTweetsToCsv() {

        TweetAuditService service = new TweetAuditService(
                tweetUrlBuilder,
                csvReportWriter,
                tweetAuditClient,
                checkpointService);

        // Create a temporary report.csv file path
        Path outputPath = tempDir.resolve("report.csv");

        Path checkpointPath = tempDir.resolve("checkpoint.txt");

        // Create two fake tweets for the test and put them in a list so the service can audit them
        Tweet flaggedTweet = new Tweet("111", "hello", "date");
        Tweet safeTweet = new Tweet("222", "another tweet", "date");

        List<Tweet> tweets = List.of(flaggedTweet, safeTweet);

        // username used to build the tweet URLs
        String username = "marv";

        // Tells the mock audit that the first tweet is flagged and the second is safe
        when(tweetAuditClient.audit(flaggedTweet))
                .thenReturn(new AuditDecision(true, "flagged"));
        when(tweetAuditClient.audit(safeTweet))
                .thenReturn(new AuditDecision(false, "safe"));

        // Telling rhe mock URL builder what to return when it's called by the service
        when(tweetUrlBuilder.build("marv", "111"))
                .thenReturn("https://x.com/marv/status/111");

        // mock the checkpoint loading
        when(checkpointService.loadProcessedTweetIds(checkpointPath))
                .thenReturn(Set.of());

        // Call the service method being tested
        service.audit(tweets, username, outputPath, checkpointPath);


        // Verify that the CSV writer was called for the flagged tweet
        verify(csvReportWriter).writeFlaggedTweet(
                outputPath,
                "https://x.com/marv/status/111"
        );

        // Verify that the CSV writer was not called for the flagged tweet
        verify(csvReportWriter, never()).writeFlaggedTweet(
                outputPath,
                "https://x.com/marv/status/222"
        );

        // Verify that both processed tweets were marked
        verify(checkpointService).markProcessed(checkpointPath, "111");
        verify(checkpointService).markProcessed(checkpointPath, "222");
    }
}