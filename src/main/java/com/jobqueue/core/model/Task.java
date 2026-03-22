package com.jobqueue.core.model;

public interface Task {

    void execute();

    int getPriority();

    int getId();
}
