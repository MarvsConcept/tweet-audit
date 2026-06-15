package com.marv.tweet_audit.writer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CsvReportWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldCreateCsvFileWithHeaderAndTweet() throws Exception {

        CsvReportWriter writer = new CsvReportWriter();

        // Create a temporary report.csv file path
        Path outputPath = tempDir.resolve("report.csv");

        // Calls the method to create the report.csv and write the flagged tweet into it.
        writer.writeFlaggedTweet(
                outputPath,
                "https://x.com/user/status/123"
        );

        // Reads the content of the file
        String content = Files.readString(outputPath);

        // Checks that the content of the file matches the expected string
        assertThat(content).isEqualTo(
                "tweet_url,deleted\n" +
                        "https://x.com/user/status/123,false\n"
        );
    }

    @Test
    void shouldAppendTweetIfFileAlreadyExists() throws Exception {

        CsvReportWriter writer = new CsvReportWriter();

        // Create a temporary report.csv file path
        Path outputPath = tempDir.resolve("report.csv");

        // Calls the method to create the report.csv and write the flagged tweet into it.
        writer.writeFlaggedTweet(
                outputPath,
                "https://x.com/user/status/123"
        );
        writer.writeFlaggedTweet(
                outputPath,
                "https://x.com/user/status/456"
        );

        // Reads the content of the file
        String content = Files.readString(outputPath);

        // Checks that the content of the file matches the expected string
        assertThat(content).isEqualTo("tweet_url,deleted\n" +
                "https://x.com/user/status/123,false\n" +
                "https://x.com/user/status/456,false\n"
        );
    }
}