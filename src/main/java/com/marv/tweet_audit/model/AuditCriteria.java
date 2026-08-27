package com.marv.tweet_audit.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "audit.criteria")
public class AuditCriteria {

    private List<String> forbiddenWords;

    private boolean professionalCheck;

    private String tone;

    private boolean excludePolitics;
}
