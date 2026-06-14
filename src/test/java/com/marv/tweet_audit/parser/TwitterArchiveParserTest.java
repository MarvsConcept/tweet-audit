package com.marv.tweet_audit.parser;

import com.marv.tweet_audit.model.Tweet;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TwitterArchiveParserTest {

    @Test
    void shouldParseTweetsFromArchiveFile() {

        TwitterArchiveParser parser = new TwitterArchiveParser();

        List<Tweet> tweets = parser.parse(
                Path.of("src/test/resources/sample/tweets.js")
        );

        // expected to find exactly 2 tweets
        assertEquals(2, tweets.size());

        // expets the first tweet ID to be correct
        assertEquals("1234567890", tweets.get(0).id());
        assertEquals("I love crypto and NFT", tweets.get(0).text());

        // expects the second tweet ID to be correct
        assertEquals("9876543210", tweets.get(1).id());
        assertEquals("Learning backend engineering today", tweets.get(1).text());
    }

}