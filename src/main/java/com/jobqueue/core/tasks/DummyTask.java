package com.jobqueue.core.tasks;
import com.jobqueue.core.model.Task;

public class DummyTask implements Task {

    private int taskId;
    private int sleepDuration;
    private final int priority;

    public DummyTask(int task_id, int sleep_duration, int priority) {
        this.taskId = task_id;
        this.sleepDuration = sleep_duration;
        this.priority = priority;
    }

    @Override
    public void execute()  {

        try {
            int actualSleep = (this.sleepDuration == 0) ? 2000 : this.sleepDuration;

            System.out.println("Task " + taskId + " started");
            Thread.sleep(actualSleep);
            System.out.println("Task " + taskId + " finished.");
        }

        catch (InterruptedException e) {
            System.out.println("Task " + taskId + " was interrupted");
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public int getId() {
        return taskId;
    }

    public void setId(int task_id) {
        this.taskId = task_id;
    }

    public int getSleepDuration() {
        return sleepDuration;
    }

    public void setSleepDuration(int sleep_duration) {
        this.sleepDuration = sleep_duration;
    }

}
