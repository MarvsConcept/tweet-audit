package com.marv.tweet_audit.checkpoint;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CheckpointService {

    // Read checkpoint.txt and return all the tweet Ids that has been processed already.
    public Set<String> loadProcessedTweetIds(Path checkpointPath) {

        try {
            // checks if checkpoint.txt has not been created yet.
            if (Files.notExists(checkpointPath)) {
                return Set.of();
            }

            // reads each line as one tweet Id.
            // ignores empty lines and turns the list of lines into a Set<String>.
            return Files.readAllLines(checkpointPath)
                    .stream()
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toSet());

        } catch (IOException e) {
            throw new RuntimeException("Failed to load checkpoint file", e);
        }
    }

    // After finishing a tweet, save its ID to checkpoint.txt.
    public void markProcessed(Path checkpointPath, String tweetId) {

    }
}
