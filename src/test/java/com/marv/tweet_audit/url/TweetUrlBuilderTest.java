package com.marv.tweet_audit.url;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TweetUrlBuilderTest {

    @Test
    void shouldBuildTweetUrl() {
        TweetUrlBuilder builder = new TweetUrlBuilder();

        String url = builder.build("marvade", "1234567890");

        assertEquals("https://x.com/marvade/status/1234567890", url);
    }

}