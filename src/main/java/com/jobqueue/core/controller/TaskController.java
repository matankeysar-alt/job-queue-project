package com.jobqueue.core.controller;

import com.jobqueue.core.dto.QueueStatusDTO;
import com.jobqueue.core.dto.TaskRequestDTO;
import com.jobqueue.core.dto.openAi.TaskResponseDTO;
import com.jobqueue.core.entity.TaskEntity;
import com.jobqueue.core.queue.SmartTaskQueue;
import com.jobqueue.core.model.Task;
import com.jobqueue.core.repository.TaskRepository;
import com.jobqueue.core.service.OpenAiService;
import com.jobqueue.core.tasks.AiSummarizeTask;
import com.jobqueue.core.tasks.DummyTask;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for managing tasks in the job queue.
 * Acts as the entry point for client applications to submit and monitor tasks.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final SmartTaskQueue taskQueue;
    private final TaskRepository taskRepository;
    private final OpenAiService openAiService;
    private final AtomicInteger taskIdGenerator = new AtomicInteger(1);

    public TaskController(SmartTaskQueue taskQueue, TaskRepository taskRepository, OpenAiService openAiService) {
        this.taskQueue = taskQueue;
        this.taskRepository = taskRepository;
        this.openAiService = openAiService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTask(@RequestBody TaskRequestDTO request) {
        // Generate a thread-safe unique ID
        int taskId = taskIdGenerator.getAndIncrement();

        // Persist task details to the database
        TaskEntity entity = new TaskEntity(taskId, request.type(), "PENDING");
        taskRepository.save(entity);

        int sleep = request.sleepDuration() != 0 ? request.sleepDuration() : 2000;
        int priority = request.priority() != 0 ? request.priority() : 10;
        // --------------------------------------------------------

        Task task;
        // Fix: Apply Polymorphism based on the requested task type
        if ("DUMMY".equalsIgnoreCase(request.type()) || "DUMMY_TASK".equalsIgnoreCase(request.type())) {
            task = new DummyTask(taskId, sleep, priority);
        } else {
            task = new AiSummarizeTask(taskId, request.text(), priority, this.openAiService);
        }

        // Attempt to add it to the queue
        boolean isEnqueued = taskQueue.submitTask(task);

        if (isEnqueued) {
            return ResponseEntity.ok("Task [" + taskId + "] of type '" + request.type() + "' with Priority [" + priority + "] saved and enqueued.");
        } else {
            /* Applying Backpressure */
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Backpressure: Task " + taskId + " saved to DB but queue is full. Try again later.");
        }
    }

    @GetMapping("/status")
    public ResponseEntity<QueueStatusDTO> getStatus() {
        QueueStatusDTO status = new QueueStatusDTO(
                taskQueue.getPendingTasksCount(),
                taskQueue.getRemainingCapacity(),
                taskQueue.getMaxCapacity()
        );
        return ResponseEntity.ok(status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable int id) {
        return taskRepository.findById(id)
                .map(entity -> new TaskResponseDTO(
                        entity.getId(),
                        entity.getTaskType(),
                        entity.getStatus(),
                        entity.getResult()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        List<TaskResponseDTO> tasks = taskRepository.findAll().stream()
                .map(entity -> new TaskResponseDTO(
                        entity.getId(),
                        entity.getTaskType(),
                        entity.getStatus(),
                        entity.getResult()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(tasks);
    }
}