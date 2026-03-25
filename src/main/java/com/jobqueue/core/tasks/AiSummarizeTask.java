package com.jobqueue.core.tasks;

import com.jobqueue.core.model.Task;
import com.jobqueue.core.service.OpenAiService;

/**
 * This class represents a concrete implementation of a Task.
 * It encapsulates the logic required to send text to the OpenAI API
 * and retrieve a summarized version of it.
 */
public class AiSummarizeTask implements Task {

    private final int taskId;
    private final int priority;
    private final String textToSummarize;

    // The service that handles the actual HTTP communication
    private final OpenAiService openAiService;

    // Stores the final result after the AI processing is complete.
    private String summaryResult;

    // We updated the constructor to receive the OpenAiService
    public AiSummarizeTask(int taskId, String textToSummarize, int priority, OpenAiService openAiService) {
        this.taskId = taskId;
        this.textToSummarize = textToSummarize;
        this.priority = priority;
        this.openAiService = openAiService;
    }

    @Override
    public int getId() {
        return taskId;
    }

    public int getPriority() {
        return priority;
    }

    /**
     * The core execution logic triggered by the worker threads in the ThreadPool.
     * This is where the heavy lifting (HTTP call to OpenAI) happens.
     */
    @Override
    public void execute() {
        System.out.println("Starting AI Summarize Task for ID: " + taskId);

        try {
            // This is synchronous and will block ONLY the specific worker thread executing this task.
            this.summaryResult = openAiService.summarizeText(textToSummarize);

            System.out.println("AI Task [" + taskId + "] completed successfully! Result is ready to be saved to DB.");

        } catch (Exception e) {
            System.err.println("AI Task [" + taskId + "] failed: " + e.getMessage());
        }
    }

    public String getSummaryResult() {
        return summaryResult;
    }
}