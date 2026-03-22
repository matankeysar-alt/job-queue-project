package com.jobqueue.core.queue;
import com.jobqueue.core.model.Task;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import org.springframework.stereotype.Component;

@Component
public class SmartTaskQueue {

    private final int MAX_CAPACITY = 100;
    private final PriorityBlockingQueue<Task> queue;

    public SmartTaskQueue() {

        Comparator<Task> comp = (Task t1, Task t2) -> {
            int p1 = t1.getPriority();
            int p2 = t2.getPriority();
            return Integer.compare(p1, p2); // lower integer means higher priority.
        };
        this.queue = new PriorityBlockingQueue<Task>(MAX_CAPACITY, comp);
    }

    public boolean submitTask(Task task) {

        if (queue.size() < MAX_CAPACITY) {
            return queue.offer(task);
        }
        else {
            return false;
        }
    }

    public Task takeTask() throws InterruptedException {
        return queue.take(); // blocking method - if the queue is empty, the thread is blocked.
    }

    public int getPendingTasksCount() {
        return queue.size();
    }

    public int getMaxCapacity() {
        return MAX_CAPACITY;
    }

    public int getRemainingCapacity() {
        return MAX_CAPACITY - queue.size();
    }

}
