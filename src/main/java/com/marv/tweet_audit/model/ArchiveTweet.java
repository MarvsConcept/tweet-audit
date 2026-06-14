package com.marv.tweet_audit.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ArchiveTweet(

        String id,

        @JsonProperty("full_text")
        String fullText,

        @JsonProperty("created_at")
        String createdAt
) {
}
