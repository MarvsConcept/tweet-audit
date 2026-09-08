package com.marv.tweet_audit.writer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FailedTweetWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldWriteFailedTweetId() throws Exception {

        FailedTweetWriter writer = new FailedTweetWriter();

        Path failedTweetPath = tempDir.resolve("failed_tweets.txt");

        // Write a failed tweet Id
        writer.writeFailedTweet(failedTweetPath, "111");

        // Read the generated file
        String content = Files.readString(failedTweetPath);

        // Verify the failed tweet Id was saved
        assertThat(content)
                .isEqualTo("111" + System.lineSeparator());
    }

}