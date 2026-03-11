package com.jobqueue.core.engine;
import com.jobqueue.core.model.Task;
import com.jobqueue.core.queue.SmartTaskQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobEngine {

    private final SmartTaskQueue queue;
    private final ExecutorService threadPool;

    public JobEngine(SmartTaskQueue queue) {
        this.queue = queue;
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    public void start() {

        Thread dispatcherThread = new Thread(() -> {
            while (true) {
                try {
                    Task task = queue.takeTask();
                    threadPool.submit(task::execute);
                }

                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Engine interrupted, Stopping dispatcher...");
                    break;
                }
            }
        });

        dispatcherThread.start();
    }
}
