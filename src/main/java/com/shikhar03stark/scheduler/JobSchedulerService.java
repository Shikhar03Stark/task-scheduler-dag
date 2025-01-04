package com.shikhar03stark.scheduler;

import com.shikhar03stark.scheduler.model.Job;

import java.io.Closeable;

public interface JobSchedulerService extends Runnable, Closeable {
    boolean accept(Job job);
}
