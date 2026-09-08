package com.marv.tweet_audit.writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FailedTweetWriter {

    public void writeFailedTweet(Path path, String tweetId) {

        try {
            Files.createDirectories(path.getParent());

            Files.writeString(
                    path,
                    tweetId + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to write failed tweet", e);
        }
    }
}
