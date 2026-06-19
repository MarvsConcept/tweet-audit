package com.marv.tweet_audit.url;

import org.springframework.stereotype.Component;

@Component
public class TweetUrlBuilder {

    public String build(String username, String tweetId) {

        return "https://x.com/" + username + "/status/" + tweetId;
    }
}
