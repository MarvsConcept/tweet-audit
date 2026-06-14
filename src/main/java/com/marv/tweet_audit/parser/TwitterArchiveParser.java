package com.marv.tweet_audit.parser;

import com.marv.tweet_audit.model.Tweet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// read tweets.js,
// remove the JavaScript prefix and convert the remaining JSON to Java objects
// Return List<Tweet>

public class TwitterArchiveParser {

    public List<Tweet> parse(Path tweetFilePath) {

        try {
            String content = Files.readString(tweetFilePath);
            System.out.println(content);
            return List.of();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read tweets file", e);
        }
    }
}
