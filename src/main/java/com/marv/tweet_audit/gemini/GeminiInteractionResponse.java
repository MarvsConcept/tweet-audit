package com.marv.tweet_audit.gemini;

import java.util.List;

public record GeminiInteractionResponse(

        String status,

        List<GeminiStep> steps
){
}
