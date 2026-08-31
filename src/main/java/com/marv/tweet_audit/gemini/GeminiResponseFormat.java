package com.marv.tweet_audit.gemini;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record GeminiResponseFormat(

        String type,

        // Want the text to be valid JSON
        @JsonProperty("mime_type")
        String mineType,

        // Json schema that describes the shape of the response
        Map<String, Object> schema
) {
}
