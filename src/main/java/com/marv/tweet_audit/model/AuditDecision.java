package com.marv.tweet_audit.model;

public record AuditDecision(
        boolean flagged,
        String reason
) {
}
