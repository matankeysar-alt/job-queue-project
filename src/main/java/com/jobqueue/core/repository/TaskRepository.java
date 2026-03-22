package com.jobqueue.core.repository;

import com.jobqueue.core.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
    // save(), findById(), findAll(), deleteById()...
}