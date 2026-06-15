package com.marv.tweet_audit.parser;

import com.marv.tweet_audit.model.Tweet;
import com.marv.tweet_audit.model.TweetWrapper;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// read tweets.js,
// remove the JavaScript prefix and convert the remaining JSON to Java objects
// Return List<Tweet>

public class TwitterArchiveParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Tweet> parse(Path tweetFilePath) {

        try {
            // reads the entire file into a String
            String content = Files.readString(tweetFilePath);

            // remove the Javascript prefix so that only valid Json remains
            String json = content.replace("window.YTD.tweets.part0 = ", "");

            // Converting JSON string to list of Java Objects
            List<TweetWrapper> wrappers = objectMapper.readValue(json,
                    new TypeReference<List<TweetWrapper>>() {}
            );

            // Converting eact TweetWrapper to the Tweet record.
            return wrappers.stream()
                    .map(wrapper -> new Tweet(
                            wrapper.tweet().id(),
                            wrapper.tweet().fullText(),
                            wrapper.tweet().createdAt()
                    ))
                            .toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read tweets file", e);
        }
    }
}
