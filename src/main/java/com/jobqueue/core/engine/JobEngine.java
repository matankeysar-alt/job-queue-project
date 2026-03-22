package com.jobqueue.core.engine;

import com.jobqueue.core.entity.TaskEntity;
import com.jobqueue.core.model.Task;
import com.jobqueue.core.queue.SmartTaskQueue;
import com.jobqueue.core.repository.TaskRepository;
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
        updateTaskStatus(task.getId(), "RUNNING");

        try {
            // 2. Perform the actual work
            task.execute();

            // 3. Update status to COMPLETED upon success
            updateTaskStatus(task.getId(), "COMPLETED");
        } catch (Exception e) {
            // 4. Update status to FAILED if an error occurs
            updateTaskStatus(task.getId(), "FAILED");
            System.err.println("Task [" + task.getId() + "] failed: " + e.getMessage());
        }
    }

    private void updateTaskStatus(int taskId, String newStatus) {
        taskRepository.findById(taskId).ifPresent(entity -> {
            entity.setStatus(newStatus);
            taskRepository.save(entity);
            System.out.println("Database Updated: Task [" + taskId + "] is now " + newStatus);
        });
    }
}