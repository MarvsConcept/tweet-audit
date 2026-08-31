package com.marv.tweet_audit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "gemini")
public class GeminiProperties{

    private String apiKey;

    private String model;

    private String baseUrl;
}
