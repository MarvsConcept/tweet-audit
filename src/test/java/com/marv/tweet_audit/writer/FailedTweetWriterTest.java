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

    @Test
    void shouldNotWriteDuplicateFailedTweetIds() throws Exception {
        FailedTweetWriter writer = new FailedTweetWriter();

        Path failedTweetsPath = tempDir.resolve("failed_tweets.txt");

        // Same tweet fails twice
        writer.writeFailedTweet(failedTweetsPath, "111");
        writer.writeFailedTweet(failedTweetsPath, "111");

        String content = Files.readString(failedTweetsPath);

        // ID should appear only once
        assertThat(content)
                .isEqualTo("111" + System.lineSeparator());
    }

    @Test
    void shouldRemoveFailedTweetAfterSuccessfulRetry() throws Exception {
        FailedTweetWriter writer = new FailedTweetWriter();

        Path path = tempDir.resolve("failed_tweets.txt");

        writer.writeFailedTweet(path, "111");
        writer.writeFailedTweet(path, "222");

        // Tweet 111 later succeeds
        writer.removeFailedTweet(path, "111");

        String content = Files.readString(path);

        assertThat(content)
                .isEqualTo("222" + System.lineSeparator());
    }
}