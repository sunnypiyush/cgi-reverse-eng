package com.taskmanager.controller;

import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * REST controller for Task operations.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    
    private final TaskRepository taskRepository;
    
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    /**
     * GET /api/tasks - Get all tasks
     * 
     * @return List of all tasks
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        try {
            List<Task> tasks = taskRepository.findAll();
            logger.info("Retrieved {} tasks", tasks.size());
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Failed to retrieve tasks", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to retrieve tasks", e);
        }
    }
    
    /**
     * POST /api/tasks - Create a new task
     * 
     * @param task Task to create
     * @return Created task
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        try {
            // Sanitize task text to prevent XSS
            if (task.getText() != null) {
                task.setText(sanitizeHtml(task.getText()));
            }
            
            Task savedTask = taskRepository.save(task);
            logger.info("Created task with ID: {}", savedTask.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
        } catch (Exception e) {
            logger.error("Failed to create task", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to create task", e);
        }
    }
    
    /**
     * DELETE /api/tasks/{id} - Delete a task by ID
     * 
     * @param id Task ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        try {
            boolean deleted = taskRepository.deleteById(id);
            if (deleted) {
                logger.info("Deleted task with ID: {}", id);
                return ResponseEntity.noContent().build();
            } else {
                logger.warn("Task not found with ID: {}", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Task not found with ID: " + id);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to delete task", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to delete task", e);
        }
    }
    
    /**
     * DELETE /api/tasks - Clear all tasks
     * 
     * @return No content
     */
    @DeleteMapping
    public ResponseEntity<Void> clearAllTasks() {
        try {
            taskRepository.deleteAll();
            logger.info("Cleared all tasks");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to clear all tasks", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to clear all tasks", e);
        }
    }
    
    /**
     * POST /api/tasks/reload - Reload tasks from file
     * 
     * @return Reloaded tasks with count message
     */
    @PostMapping("/reload")
    public ResponseEntity<Map<String, Object>> reloadTasks() {
        try {
            List<Task> tasks = taskRepository.findAll();
            logger.info("Reloaded {} tasks from file", tasks.size());
            
            Map<String, Object> response = Map.of(
                    "message", "Reloaded " + tasks.size() + " tasks from file",
                    "count", tasks.size(),
                    "tasks", tasks
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to reload tasks", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to reload tasks", e);
        }
    }
    
    /**
     * Sanitize HTML to prevent XSS attacks.
     * Encodes special HTML characters.
     * 
     * @param input Input string
     * @return Sanitized string
     */
    private String sanitizeHtml(String input) {
        if (input == null) {
            return null;
        }
        
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;")
                .replace("/", "&#x2F;");
    }
}
