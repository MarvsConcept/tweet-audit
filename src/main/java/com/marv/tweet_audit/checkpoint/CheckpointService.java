package com.marv.tweet_audit.checkpoint;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Set;

@Component
public class CheckpointService {

    // Read checkpoint.txt and return all the tweet Ids that has been processed already.
    public Set<String> loadProcessedTweetIds(Path checkpointPath) {

        return Set.of();
    }

    // After finishing a tweet, save it's ID to checkpoint.txt.
    public void markProcessed(Path checkpointPath, String tweetId) {

    }
}
