package com.jobqueue.core.dto.openAi;

/**
 * DTO representing the full state of a task, including its result.
 */
public record TaskResponseDTO(Integer id, String type, String status, String result) {
}