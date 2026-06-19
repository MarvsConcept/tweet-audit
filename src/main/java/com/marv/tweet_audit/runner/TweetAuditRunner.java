package com.marv.tweet_audit.runner;

import com.marv.tweet_audit.audit.FakeTweetAuditClient;
import com.marv.tweet_audit.audit.TweetAuditClient;
import com.marv.tweet_audit.model.AuditDecision;
import com.marv.tweet_audit.model.Tweet;
import com.marv.tweet_audit.parser.TwitterArchiveParser;
import com.marv.tweet_audit.service.TweetAuditService;
import com.marv.tweet_audit.url.TweetUrlBuilder;
import com.marv.tweet_audit.writer.CsvReportWriter;
//import lombok.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Path;
import java.util.List;

@Component
public class TweetAuditRunner implements CommandLineRunner {

    @Value("${archive.tweets-path}")
    private String tweetsPath;

    @Value("${x.username}")
    private String username;

    @Value("${output.csv-path}")
    private String outputPath;

    @Override
    public void run(String... args) {

        TwitterArchiveParser parser = new TwitterArchiveParser();
        TweetUrlBuilder tweetUrlBuilder = new TweetUrlBuilder();
        CsvReportWriter csvReportWriter = new CsvReportWriter();
        TweetAuditClient auditClient = new FakeTweetAuditClient();

        TweetAuditService service = new TweetAuditService(
                tweetUrlBuilder,
                csvReportWriter,
                auditClient
        );

        // calls the parser method and points to the tweet file
        List<Tweet> tweets = parser.parse(Path.of(tweetsPath));

        service.audit(
                tweets,
                username,
                Path.of(outputPath)
        );
    }

}
