package com.marv.tweet_audit.gemini;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GeminiInteractionRequest(

        // The Gemini model to use, e.g. gemini-3.7-flash
        String model,

        // The prompt we are sending to Gemini
        String input,

        // Tells Gemini we want JSON back
        @JsonProperty("response_format")
        GeminiResponseFormat responseFormat

) {
}
