package com.jobqueue.core.dto.openAi;

import java.util.List;

public record OpenAiRequest(String model, List<Message> messages) {
}