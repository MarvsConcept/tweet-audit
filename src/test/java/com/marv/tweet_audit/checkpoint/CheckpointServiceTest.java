package com.marv.tweet_audit.checkpoint;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CheckpointServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldReturnEmptySetWhenCheckpointFileDoesNotExist() {

        CheckpointService checkpointService = new CheckpointService();
        Path checkpointPath = tempDir.resolve("checkpoint.txt");

        Set<String> processedTweetIds = checkpointService.loadProcessedTweetIds(checkpointPath);

        assertThat(processedTweetIds).isEmpty();
    }

}