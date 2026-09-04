package com.marv.tweet_audit.gemini;

public record GeminiContent(

        // Example: "text"
        String type,

        // The actual response text from Gemini
        String text
) {
}
