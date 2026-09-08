package com.marv.tweet_audit.writer;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FailedTweetWriter {

    public void writeFailedTweet(Path path, String tweetId) {

        try {
            // Make sure the parent directory exists
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            // If the file already exists, check whether this tweet was recorded before
            if (Files.exists(path)) {
                Set<String> failedTweetIds = Files.readAllLines(path)
                        .stream()
                        .filter(line -> !line.isBlank())
                        .collect(Collectors.toSet());

                // Don't write duplicate failure entries
                if (failedTweetIds.contains(tweetId)) {
                    return;
                }
            }

            // Record the failed tweet
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
