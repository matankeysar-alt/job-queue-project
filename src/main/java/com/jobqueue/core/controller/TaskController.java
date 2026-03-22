package com.jobqueue.core.controller;

import com.jobqueue.core.entity.TaskEntity;
import com.jobqueue.core.queue.SmartTaskQueue;
import com.jobqueue.core.model.Task;
import com.jobqueue.core.repository.TaskRepository;
import com.jobqueue.core.tasks.DummyTask;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * REST Controller for managing tasks in the job queue.
 * Acts as the entry point for client applications to submit and monitor tasks.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final SmartTaskQueue taskQueue;
    private final TaskRepository taskRepository;
    private final AtomicInteger taskIdGenerator = new AtomicInteger(1);

    /**
     * Constructor-based dependency injection.
     * Spring automatically injects the required beans at runtime.
     */
    public TaskController(SmartTaskQueue taskQueue, TaskRepository taskRepository) {
        this.taskQueue = taskQueue;
        this.taskRepository = taskRepository;
    }

    /**
     * Endpoint to add a new task.
     * Workflow: Generate ID -> Persist to DB -> Enqueue in RAM -> Check for Backpressure.
     *
     * @param request Data Transfer Object containing the task details.
     * @return ResponseEntity indicating success or service unavailability (Queue full).
     */
    @PostMapping("/add")
    public ResponseEntity<String> addTask(@RequestBody TaskRequestDTO request) {
        // Generate a thread-safe unique ID
        int taskId = taskIdGenerator.getAndIncrement();

        // Persist task details to the database (Ensures data persistence)
        // Using the task type provided by the client in the request body
        TaskEntity entity = new TaskEntity(taskId, request.type(), "PENDING");
        taskRepository.save(entity);

        // Create the logical task object and attempt to add it to the queue
        Task task = new DummyTask(taskId, 5000, 10);
        boolean isEnqueued = taskQueue.submitTask(task);

        if (isEnqueued) {
            return ResponseEntity.ok("Task [" + taskId + "] of type '" + request.type() + "' saved and enqueued.");
        } else {
            /* * Applying Backpressure: If the queue is full, we return 503 Service Unavailable.
             * The task remains in the DB as PENDING for future recovery.
             */
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Backpressure: Task " + taskId + " saved to DB but queue is full. Try again later.");
        }
    }

    /**
     * Endpoint to retrieve the current health and capacity of the task queue.
     *
     * @return ResponseEntity containing a QueueStatusDTO mapped to JSON.
     */
    @GetMapping("/status")
    public ResponseEntity<QueueStatusDTO> getStatus() {
        QueueStatusDTO status = new QueueStatusDTO(
                taskQueue.getPendingTasksCount(),
                taskQueue.getRemainingCapacity(),
                taskQueue.getMaxCapacity()
        );
        return ResponseEntity.ok(status);
    }
}