package com.jobqueue.core.engine;

import com.jobqueue.core.entity.TaskEntity;
import com.jobqueue.core.model.Task;
import com.jobqueue.core.queue.SmartTaskQueue;
import com.jobqueue.core.repository.TaskRepository;
import com.jobqueue.core.tasks.AiSummarizeTask; // Import the specific task type
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class JobEngine {

    private final SmartTaskQueue queue;
    private final ExecutorService threadPool;
    private final TaskRepository taskRepository;

    public JobEngine(SmartTaskQueue queue, TaskRepository taskRepository) {
        this.queue = queue;
        this.taskRepository = taskRepository;
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    /**
     * Automatically starts the engine after the Spring Bean is fully initialized.
     */
    @PostConstruct
    public void start() {
        Thread dispatcherThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Task task = queue.takeTask();
                    // Wrapping the execution with status updates
                    threadPool.submit(() -> processTask(task));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Engine interrupted, Stopping dispatcher...");
                    break;
                }
            }
        });
        dispatcherThread.start();
    }

    /**
     * Internal helper to manage the task lifecycle in the database.
     */
    private void processTask(Task task) {
        // 1. Update status to RUNNING in the database
        updateTaskInDb(task, "RUNNING");

        try {
            // 2. Perform the actual work (HTTP call to OpenAI happens here)
            task.execute();

            // 3. Update status to COMPLETED and save the result
            updateTaskInDb(task, "COMPLETED");
        } catch (Exception e) {
            // 4. Update status to FAILED if an error occurs
            updateTaskInDb(task, "FAILED");
            System.err.println("Task [" + task.getId() + "] failed: " + e.getMessage());
        }
    }

    /**
     * Updated helper to handle both status updates and result persistence.
     */
    private void updateTaskInDb(Task task, String newStatus) {
        taskRepository.findById(task.getId()).ifPresent(entity -> {
            entity.setStatus(newStatus);

            // If the task is an AI task, we extract the result and save it to the entity
            if (task instanceof AiSummarizeTask summarizeTask) {
                String result = summarizeTask.getSummaryResult();
                entity.setResult(result);
            }

            taskRepository.save(entity);
            System.out.println("Database Updated: Task [" + task.getId() + "] is now " + newStatus);
        });
    }
}