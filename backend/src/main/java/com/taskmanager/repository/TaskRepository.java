package com.taskmanager.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.taskmanager.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Repository for Task entities with JSON file storage.
 */
@Repository
public class TaskRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskRepository.class);
    
    @Value("${app.storage.tasks-file}")
    private String tasksFilePath;
    
    private final JsonFileRepository<Task> fileRepository;
    
    public TaskRepository(JsonFileRepository<Task> fileRepository) {
        this.fileRepository = fileRepository;
    }
    
    /**
     * Find all tasks.
     * 
     * @return List of all tasks
     */
    public List<Task> findAll() {
        try {
            return fileRepository.readAll(tasksFilePath, new TypeReference<List<Task>>() {});
        } catch (IOException e) {
            logger.error("Failed to read tasks from file", e);
            throw new RuntimeException("Failed to read tasks", e);
        }
    }
    
    /**
     * Find task by ID.
     * 
     * @param id Task ID
     * @return Optional containing the task if found
     */
    public Optional<Task> findById(String id) {
        List<Task> tasks = findAll();
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }
    
    /**
     * Save a new task.
     * 
     * @param task Task to save (ID and created timestamp will be generated)
     * @return Saved task with generated ID and timestamp
     */
    public Task save(Task task) {
        try {
            List<Task> tasks = findAll();
            
            // Generate ID if not present
            if (task.getId() == null || task.getId().isEmpty()) {
                task.setId(generateTaskId());
            }
            
            // Set created timestamp if not present
            if (task.getCreated() == null) {
                task.setCreated(Instant.now());
            }
            
            tasks.add(task);
            fileRepository.writeAll(tasksFilePath, tasks);
            
            logger.info("Saved task with ID: {}", task.getId());
            return task;
        } catch (IOException e) {
            logger.error("Failed to save task", e);
            throw new RuntimeException("Failed to save task", e);
        }
    }
    
    /**
     * Delete task by ID.
     * 
     * @param id Task ID to delete
     * @return true if task was deleted, false if not found
     */
    public boolean deleteById(String id) {
        try {
            List<Task> tasks = findAll();
            int initialSize = tasks.size();
            
            tasks = tasks.stream()
                    .filter(task -> !task.getId().equals(id))
                    .collect(Collectors.toList());
            
            if (tasks.size() < initialSize) {
                fileRepository.writeAll(tasksFilePath, tasks);
                logger.info("Deleted task with ID: {}", id);
                return true;
            }
            
            return false;
        } catch (IOException e) {
            logger.error("Failed to delete task", e);
            throw new RuntimeException("Failed to delete task", e);
        }
    }
    
    /**
     * Delete all tasks.
     */
    public void deleteAll() {
        try {
            fileRepository.writeAll(tasksFilePath, List.of());
            logger.info("Deleted all tasks");
        } catch (IOException e) {
            logger.error("Failed to delete all tasks", e);
            throw new RuntimeException("Failed to delete all tasks", e);
        }
    }
    
    /**
     * Count total number of tasks.
     * 
     * @return Number of tasks
     */
    public long count() {
        return findAll().size();
    }
    
    /**
     * Generate unique task ID.
     * Format: timestamp-uuid
     * 
     * @return Generated task ID
     */
    private String generateTaskId() {
        long timestamp = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return timestamp + "-" + uuid;
    }
}
