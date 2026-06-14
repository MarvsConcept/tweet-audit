package com.marv.tweet_audit.runner;

import com.marv.tweet_audit.parser.TwitterArchiveParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;



@Component
public class TweetAuditRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        TwitterArchiveParser parser = new TwitterArchiveParser();

        // calls the parser method and points to the tweet file
        parser.parse(Path.of("src/main/resources/sample/tweets.js"));
    }

}
