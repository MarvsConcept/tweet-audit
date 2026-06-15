package com.marv.tweet_audit.service;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TweetAuditServiceTest {

    @TempDir
    Path tempDir;

    @Mock
    private TweetUrlBuilder tweetUrlBuilder;
    @Mock
    private CsvReportWriter csvReportWriter;

    // List of tweets that the service will audit
    List<Tweet> tweets = List.of(
            new Tweet("111", "hello", "date"),
            new Tweet("222", "another tweet", "date")
    );

    // username used to build the tweet URLs
    String username = "marv";

    @Test
    void shouldBuildTweetUrlsAndWriteThemToCsv() {

        TweetAuditService service = new TweetAuditService(
                tweetUrlBuilder,
                csvReportWriter);

        Path outputPath = tempDir.resolve("report.csv");

        // Telling rhe mock URL builder what to return when it's called by the service
        when(tweetUrlBuilder.build("marv", "111"))
                .thenReturn("https://x.com/marv/status/111");

        when(tweetUrlBuilder.build("marv", "222"))
                .thenReturn("https://x.com/marv/status/222");


        // Call the service method being tested
        service.audit(tweets, username, outputPath);

        // Verify that the CSV writer was called
        // (proves that the service build the urls and passed them to the Csv writer)
        verify(csvReportWriter).writeFlaggedTweet(
                outputPath,
                "https://x.com/marv/status/111"
        );
        verify(csvReportWriter).writeFlaggedTweet(
                outputPath,
                "https://x.com/marv/status/222"
        );
    }
}