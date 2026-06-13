package com.marv.tweet_audit.model;

public record Tweet(
        String id,
        String text,
        String createdAt
) {

}
