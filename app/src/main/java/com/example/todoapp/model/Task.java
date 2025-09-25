package com.example.todoapp.model;

public class Task {
    private int id;
    private String task;
    private int status; // 0 = pending, 1 = completed

    public Task(int id, String task, int status) {
        this.id = id;
        this.task = task;
        this.status = status;
    }

    public int getId() { return id; }
    public String getTask() { return task; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}
