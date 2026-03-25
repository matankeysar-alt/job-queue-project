package com.jobqueue.core.dto.openAi;

import java.util.List;

public record OpenAiResponse(List<Choice> choices) {
}