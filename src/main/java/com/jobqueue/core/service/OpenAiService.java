package com.jobqueue.core.service;

import com.jobqueue.core.dto.openAi.Message;
import com.jobqueue.core.dto.openAi.OpenAiRequest;
import com.jobqueue.core.dto.openAi.OpenAiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class OpenAiService {

    private final RestClient restClient;

    public OpenAiService(@Value("${openai.api.key}") String apiKey) {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public String summarizeText(String textToSummarize) {
        // Create the message object
        Message userMessage = new Message("user", "Please summarize the following text: " + textToSummarize);

        // Create the request DTO
        OpenAiRequest requestDto = new OpenAiRequest("gpt-3.5-turbo", List.of(userMessage));

        System.out.println("Sending structured request to OpenAI...");

        // Execute POST request, passing the DTO, and mapping the response back to a DTO
        OpenAiResponse responseDto = restClient.post()
                .body(requestDto) // Spring automatically converts this to JSON
                .retrieve()
                .body(OpenAiResponse.class); // Spring automatically converts the JSON back to this object

        // Extract the actual summary string from the nested response structure
        if (responseDto != null && !responseDto.choices().isEmpty()) {
            return responseDto.choices().getFirst().message().content();
        }

        return "Failed to get a summary.";
    }
}