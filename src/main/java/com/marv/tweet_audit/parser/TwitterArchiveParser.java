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

            // remove the Javascript prefix so that only valid Json remains
            String json = content.replace("window.YTD.tweets.part0 = ", "");

            // print the json
            System.out.println(json);
            return List.of();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read tweets file", e);
        }
    }
}
