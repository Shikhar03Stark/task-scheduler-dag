package com.shikhar03stark.scheduler.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Task {
    private final TaskExec taskExec;
    private final Set<Task> dependsOn;

    public Task(TaskExec taskExec) {
        this.taskExec = taskExec;
        this.dependsOn = new HashSet<>();
    }

    public TaskExec getTaskExec() {
        return taskExec;
    }

    public Set<Task> getDependsOn() {
        return dependsOn;
    }

    public void dependsOnTask(Task... tasks) {
        dependsOn.addAll(Arrays.asList(tasks));
    }
}
