package com.shikhar03stark.scheduler.impl;

import com.shikhar03stark.scheduler.JobSchedulerService;
import com.shikhar03stark.scheduler.model.Job;
import com.shikhar03stark.scheduler.model.OrderedJobTaskIterator;
import com.shikhar03stark.scheduler.model.Task;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class JobSchedulerServiceImpl implements JobSchedulerService {

    private final Queue<OrderedJobTaskIterator> jobQueue;
    private final int maxWorkerThreads;
    private final List<Thread> workerThreads;
    private final Semaphore waitForCloseSemaphore;
    private boolean shouldWaitForClose;

    public JobSchedulerServiceImpl(int numOfThreads) {
        jobQueue = new ConcurrentLinkedQueue<>();
        this.maxWorkerThreads = numOfThreads;
        this.workerThreads = new ArrayList<>();
        waitForCloseSemaphore = new Semaphore(1);
        shouldWaitForClose = false;
    }

    @Override
    public boolean accept(Job job) {
        final OrderedJobTaskIterator iterator = new OrderedJobTaskIterator(job);
        if (iterator.hasNext()) {
            jobQueue.add(iterator);
            return true;
        }
        return false;
    }

    private void processJobFromQueue() {
        final OrderedJobTaskIterator jobTaskIterator = jobQueue.poll();
        if (Objects.isNull(jobTaskIterator)) {
            if (shouldWaitForClose) {
                waitForCloseSemaphore.release();
            }
            return;
        }
        System.out.println("Thread " + Thread.currentThread().threadId() + " picked job " + jobTaskIterator.getJob().getJobId());
        while (jobTaskIterator.hasNext()) {
            Task task = jobTaskIterator.next();
            System.out.println("running task from " + jobTaskIterator.getJob().getJobId());
            task.getTaskExec().run();
        }
    }

    @Override
    public void run() {
        for(int i = 0; i<maxWorkerThreads; i++) {
            final Thread t = new Thread(() -> {
                while (true) {
                    processJobFromQueue();
                }
            });
            t.start();
            this.workerThreads.add(t);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            waitForCloseSemaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        workerThreads
                .forEach(thread -> {
                    try {
                        thread.join(Duration.ofSeconds(1));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

    }

    private void markWaitForClose() {
        shouldWaitForClose = true;
    }
}
