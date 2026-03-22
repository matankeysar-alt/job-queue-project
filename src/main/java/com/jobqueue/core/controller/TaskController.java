package com.jobqueue.core.controller;

import com.jobqueue.core.queue.SmartTaskQueue;
import com.jobqueue.core.model.Task;
import com.jobqueue.core.tasks.DummyTask;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * REST Controller for managing tasks in the job queue.
 * Exposes endpoints for client applications to submit and monitor tasks.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final SmartTaskQueue taskQueue;

    // Thread-safe counter for generating unique task IDs
    private final AtomicInteger taskIdGenerator = new AtomicInteger(1);

    /**
     * Constructs the TaskController with constructor-based dependency injection.
     *
     * @param taskQueue The central task queue instance managed by Spring.
     */
    public TaskController(SmartTaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    /**
     * Handles POST requests to submit a new task to the queue.
     *
     * @return ResponseEntity with a success message or a 503 status if the queue is full.
     */
    @PostMapping
    public ResponseEntity<String> submitTaskRequest() {

        // Generate a thread-safe unique ID
        int currentId = taskIdGenerator.getAndIncrement();
        // Simulate a heavy task taking 5 seconds
        int sleepDuration = 5000;
        // Default priority (lower number = higher priority according to our comparator)
        int priority = 10;

        // Instantiate the dummy task with the correct constructor
        Task task = new DummyTask(currentId, sleepDuration, priority);

        // Attempt to enqueue the task
        boolean isEnqueued = taskQueue.submitTask(task);

        if (isEnqueued) {
            return ResponseEntity.ok("Task [" + currentId + "] added successfully to the queue.");
        } else {
            // Apply backpressure
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Server is busy: Task queue is at full capacity. Request rejected.");
        }
    }

    /**
     * Handles GET requests to retrieve the current status of the task queue.
     *
     * @return ResponseEntity containing the QueueStatusDTO mapped to a JSON format.
     */
    @GetMapping("/status")
    public ResponseEntity<QueueStatusDTO> getQueueStatus() {

        int pending = taskQueue.getPendingTasksCount();
        int remaining = taskQueue.getRemainingCapacity();
        int max = taskQueue.getMaxCapacity();

        QueueStatusDTO statusDTO = new QueueStatusDTO(pending, remaining, max);

        return ResponseEntity.ok(statusDTO);
    }
}
