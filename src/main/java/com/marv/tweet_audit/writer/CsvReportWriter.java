package com.marv.tweet_audit.writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CsvReportWriter {

    public void writeFlaggedTweet(Path outputPath, String tweetUrl) {

        try {
            // Check if the CSV file already exists
            boolean fileExists = Files.exists(outputPath);

            // if it doesn't create a new one and write the header
            if (!fileExists) {
                Files.writeString(
                        outputPath,
                        "tweet_url,deleted\n",
                        StandardOpenOption.CREATE
                );
            }

            // if it does, add to the list
            Files.writeString(
                    outputPath,
                    tweetUrl + ",false\n",
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to write CSV report", e);
        }
    }
}
