package com.marv.tweet_audit.gemini;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GeminiInteractionRequest(

        String model,

        String input,

        @JsonProperty("response_format")
        GeminiResponseFormat responseFormat

) {
}
