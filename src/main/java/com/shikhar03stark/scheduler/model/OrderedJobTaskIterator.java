package com.shikhar03stark.scheduler.model;

import java.util.*;

public class OrderedJobTaskIterator implements Iterator<Task> {

    private final Job job;
    private List<Task> taskSequence;
    private final Iterator<Task> taskIterator;

    public OrderedJobTaskIterator(Job job) {
        this.job = job;
        this.taskSequence = new ArrayList<>();
        buildTaskSequence();
        this.taskIterator = taskSequence.iterator();
    }

    // use khan's algo
    private void buildTaskSequence() {
        final Map<Task, Integer> inDegreeCount = new HashMap<>();
        final Map<Task, List<Task>> invertedGraph = new HashMap<>();
        for(Task task: job.getTasks()) {
            invertedGraph.putIfAbsent(task, new ArrayList<>());
            final Set<Task> dependentTasks = task.getDependsOn();
            for (Task depTask: dependentTasks) {
                inDegreeCount.putIfAbsent(depTask, 0);
                invertedGraph.putIfAbsent(depTask, new ArrayList<>());
                invertedGraph.get(depTask).add(task);
            }
            inDegreeCount.putIfAbsent(task, 0);
            inDegreeCount.put(task, inDegreeCount.get(task) + dependentTasks.size());
        }

        final Queue<Task> dagQueue = new LinkedList<>();
        for(Task task: job.getTasks()) {
            if (inDegreeCount.get(task) == 0) {
                dagQueue.add(task);
            }
        }

        final List<Task> candidateSequence = new ArrayList<>();
        while (!dagQueue.isEmpty()) {
            final Task currentTask = dagQueue.poll();
            candidateSequence.add(currentTask);
            for(Task nextTask: invertedGraph.get(currentTask)) {
                inDegreeCount.put(nextTask, inDegreeCount.get(nextTask) - 1);
                if (inDegreeCount.get(nextTask) == 0) {
                    dagQueue.add(nextTask);
                }
            }
        }

        if (candidateSequence.size() == job.getTasks().size()) {
            this.taskSequence = candidateSequence;
        }
    }

    public Job getJob() {
        return this.job;
    }

    @Override
    public boolean hasNext() {
        return taskIterator.hasNext();
    }

    @Override
    public Task next() {
        return taskIterator.next();
    }
}
