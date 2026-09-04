package com.marv.tweet_audit.gemini;

import java.util.List;

public record GeminiStep(

        // Example: "thought" or "model_output"
        String type,

        // Only model_output steps usually contain visible text content
        List<GeminiContent> content
) {
}
