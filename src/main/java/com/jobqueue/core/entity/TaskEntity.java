package com.jobqueue.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Id
    private Integer id;
    private String taskType;
    private String status;
    private LocalDateTime createdAT;

    public TaskEntity() {}

    public TaskEntity(Integer id, String taskType, String status) {
        this.id = id;
        this.taskType = taskType;
        this.status = status;
        this.createdAT = LocalDateTime.now();
    }

    //getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAT() { return createdAT; }
    public void setCreatedAT(LocalDateTime createdAT) { this.createdAT = createdAT; }
}
