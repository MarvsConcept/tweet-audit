package com.marv.tweet_audit.gemini;

import java.util.List;

public record GeminiStep(

        String type,

        List<GeminiContent> content
) {
}
