package com.shikhar03stark;

import com.shikhar03stark.scheduler.JobSchedulerService;
import com.shikhar03stark.scheduler.impl.JobSchedulerServiceImpl;
import com.shikhar03stark.scheduler.model.Job;
import com.shikhar03stark.scheduler.model.Task;

import java.io.IOException;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        final Task t1 = new Task(() -> {
            System.out.println("Hello from T1, I depend on T3");
        });
        final Task t2 = new Task(() -> {
            System.out.println("Hello from T2, I depend on T4");
        });
        final Task t3 = new Task(() -> {
            System.out.println("Hello from T3, I depend on T2");
        });
        final Task t4 = new Task(() -> {
            System.out.println("Hello from T4, I depend on NONE");
        });

        t1.dependsOnTask(t3);
        t2.dependsOnTask(t4);
        t3.dependsOnTask(t2);

        final Job execJob = new Job(Set.of(t1, t2, t3, t4));
        final Task tt1 = new Task(() -> {
            System.out.println("Hello from T1, I depend on NONE");
        });
        final Task tt2 = new Task(() -> {
            System.out.println("Hello from T2, I depend on NONE");
        });
        final Task tt3 = new Task(() -> {
            System.out.println("Hello from T3, I depend on NONE");
        });
        final Task tt4 = new Task(() -> {
            System.out.println("Hello from T4, I depend on NONE");
        });

        final Job execJob2 = new Job(Set.of(tt1, tt2, tt3, tt4));
        final JobSchedulerService jobSchedulerService = new JobSchedulerServiceImpl(4);
        jobSchedulerService.accept(execJob);
        jobSchedulerService.accept(execJob2);

        Thread schedulerThread = new Thread(jobSchedulerService);
        schedulerThread.start();

    }
}