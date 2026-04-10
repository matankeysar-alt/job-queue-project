package com.jobqueue.core.dto;

public record TaskRequestDTO(String type, String text, int priority, int sleepDuration) {}
