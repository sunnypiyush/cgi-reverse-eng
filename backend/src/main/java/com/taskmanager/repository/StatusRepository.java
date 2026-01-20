package com.taskmanager.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.taskmanager.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Repository for Status entities with JSON file storage.
 */
@Repository
public class StatusRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(StatusRepository.class);
    
    @Value("${app.storage.statuses-file}")
    private String statusesFilePath;
    
    private final JsonFileRepository<Status> fileRepository;
    
    public StatusRepository(JsonFileRepository<Status> fileRepository) {
        this.fileRepository = fileRepository;
    }
    
    /**
     * Find all statuses.
     * 
     * @return List of all statuses
     */
    public List<Status> findAll() {
        try {
            return fileRepository.readAll(statusesFilePath, new TypeReference<List<Status>>() {});
        } catch (IOException e) {
            logger.error("Failed to read statuses from file", e);
            throw new RuntimeException("Failed to read statuses", e);
        }
    }
    
    /**
     * Find status by ID.
     * 
     * @param id Status ID
     * @return Optional containing the status if found
     */
    public Optional<Status> findById(String id) {
        List<Status> statuses = findAll();
        return statuses.stream()
                .filter(status -> status.getId().equals(id))
                .findFirst();
    }
    
    /**
     * Find status by label.
     * 
     * @param label Status label
     * @return Optional containing the status if found
     */
    public Optional<Status> findByLabel(String label) {
        List<Status> statuses = findAll();
        return statuses.stream()
                .filter(status -> status.getLabel().equalsIgnoreCase(label))
                .findFirst();
    }
    
    /**
     * Save a new status.
     * 
     * @param status Status to save (ID will be generated if not present)
     * @return Saved status with generated ID
     */
    public Status save(Status status) {
        try {
            List<Status> statuses = findAll();
            
            // Generate ID if not present
            if (status.getId() == null || status.getId().isEmpty()) {
                status.setId(generateStatusId());
            }
            
            statuses.add(status);
            fileRepository.writeAll(statusesFilePath, statuses);
            
            logger.info("Saved status with ID: {}", status.getId());
            return status;
        } catch (IOException e) {
            logger.error("Failed to save status", e);
            throw new RuntimeException("Failed to save status", e);
        }
    }
    
    /**
     * Update an existing status.
     * 
     * @param status Status to update
     * @return Updated status
     */
    public Status update(Status status) {
        try {
            List<Status> statuses = findAll();
            
            // Remove old version and add updated version
            statuses = statuses.stream()
                    .filter(s -> !s.getId().equals(status.getId()))
                    .collect(Collectors.toList());
            
            statuses.add(status);
            fileRepository.writeAll(statusesFilePath, statuses);
            
            logger.info("Updated status with ID: {}", status.getId());
            return status;
        } catch (IOException e) {
            logger.error("Failed to update status", e);
            throw new RuntimeException("Failed to update status", e);
        }
    }
    
    /**
     * Delete status by ID.
     * 
     * @param id Status ID to delete
     * @return true if status was deleted, false if not found
     */
    public boolean deleteById(String id) {
        try {
            List<Status> statuses = findAll();
            int initialSize = statuses.size();
            
            statuses = statuses.stream()
                    .filter(status -> !status.getId().equals(id))
                    .collect(Collectors.toList());
            
            if (statuses.size() < initialSize) {
                fileRepository.writeAll(statusesFilePath, statuses);
                logger.info("Deleted status with ID: {}", id);
                return true;
            }
            
            return false;
        } catch (IOException e) {
            logger.error("Failed to delete status", e);
            throw new RuntimeException("Failed to delete status", e);
        }
    }
    
    /**
     * Count total number of statuses.
     * 
     * @return Number of statuses
     */
    public long count() {
        return findAll().size();
    }
    
    /**
     * Generate unique status ID.
     * Format: status-uuid
     * 
     * @return Generated status ID
     */
    private String generateStatusId() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return "status-" + uuid;
    }
}
