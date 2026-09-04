package com.marv.tweet_audit.gemini;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GeminiInteractionResponse(

        // Status can be completed, failed, incomplete, etc.
        String status,

        // Convenience field containing the final model text, if present
        @JsonProperty("output_text")
        String outputText,

        // Full interaction steps, useful as a fallback
        List<GeminiStep> steps
){
}
