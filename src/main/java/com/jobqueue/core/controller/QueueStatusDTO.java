package com.jobqueue.core.controller;

/**
 * A Data Transfer Object (DTO) representing the current state of the task queue.
 * Records in Java automatically generate a constructor, getters, equals, and hashCode.
 */
public record QueueStatusDTO(
        int pendingTasks,
        int remainingCapacity,
        int maxCapacity
) {
}
