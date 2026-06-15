package com.marv.tweet_audit.url;

public class TweetUrlBuilder {

    public String build(String username, String tweetId) {

        return "https://x.com/" + username + "/status/" + tweetId;
    }
}
