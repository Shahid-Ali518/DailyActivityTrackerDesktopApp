package org.example.dto;

public record TaskReport(
        int total,
        int completed,
        int pending,
        double completionRate
) {}

