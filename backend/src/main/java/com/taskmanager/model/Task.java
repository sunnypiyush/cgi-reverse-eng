package com.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.Objects;

/**
 * Task entity representing a work item in the task management system.
 */
public class Task {
    
    private String id;
    
    @NotBlank(message = "Task text is required")
    @Size(min = 1, max = 500, message = "Task text must be between 1 and 500 characters")
    private String text;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant created;
    
    @NotBlank(message = "Status ID is required")
    private String statusId;
    
    // Default constructor for Jackson
    public Task() {
    }
    
    // Constructor with all fields
    public Task(String id, String text, Instant created, String statusId) {
        this.id = id;
        this.text = text;
        this.created = created;
        this.statusId = statusId;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public Instant getCreated() {
        return created;
    }
    
    public void setCreated(Instant created) {
        this.created = created;
    }
    
    public String getStatusId() {
        return statusId;
    }
    
    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", created=" + created +
                ", statusId='" + statusId + '\'' +
                '}';
    }
}
