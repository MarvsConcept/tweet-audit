package com.marv.tweet_audit.checkpoint;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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

    @Test
    void shouldLoadProcessedTweetIdsFromCheckpointFile() throws Exception {

        CheckpointService checkpointService = new CheckpointService();

        Path checkpointPath = tempDir.resolve("checkpoint.txt");

        Files.writeString(
                checkpointPath,
                "111\n222\n333\n"
        );

        Set<String> processedTweetIds = checkpointService.loadProcessedTweetIds(checkpointPath);
        assertThat(processedTweetIds).contains("111","222","333");
    }

    @Test
    void shouldMarkTweetAsProcessed() throws IOException {

        CheckpointService checkpointService = new CheckpointService();

        // Arrange
        Path checkpointPath = tempDir.resolve("checkpoint.txt");
        String tweetId = "12345";

        // Act
        checkpointService.markProcessed(checkpointPath, "12345");

        // Assert
        assertTrue(Files.exists(checkpointPath));

        List<String> lines = Files.readAllLines(checkpointPath);

        assertEquals(1, lines.size());
        assertEquals(tweetId, lines.get(0));
    }


}