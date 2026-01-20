package com.taskmanager.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Status entity representing a task status/state in the task management system.
 */
public class Status {
    
    private String id;
    
    @NotBlank(message = "Status label is required")
    @Size(min = 1, max = 50, message = "Status label must be between 1 and 50 characters")
    private String label;
    
    @NotBlank(message = "Status color is required")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex color code (e.g., #4a90e2)")
    private String color;
    
    // Default constructor for Jackson
    public Status() {
    }
    
    // Constructor with all fields
    public Status(String id, String label, String color) {
        this.id = id;
        this.label = label;
        this.color = color;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return Objects.equals(id, status.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Status{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
