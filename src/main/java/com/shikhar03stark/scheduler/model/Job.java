package com.shikhar03stark.scheduler.model;

import com.shikhar03stark.scheduler.util.RandomGenerator;

import java.util.List;
import java.util.Set;

public class Job {
    private final String jobId;
    private final Set<Task> tasks;

    public Job(Set<Task> tasks) {
        jobId = String.valueOf(RandomGenerator.nextInt());
        this.tasks = tasks;
    }

    public String getJobId() {
        return jobId;
    }

    public Set<Task> getTasks() {
        return tasks;
    }
}
