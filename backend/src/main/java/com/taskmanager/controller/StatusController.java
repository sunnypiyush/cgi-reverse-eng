package com.taskmanager.controller;

import com.taskmanager.model.Status;
import com.taskmanager.repository.StatusRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for Status operations.
 */
@RestController
@RequestMapping("/api/statuses")
public class StatusController {
    
    private static final Logger logger = LoggerFactory.getLogger(StatusController.class);
    
    private final StatusRepository statusRepository;
    
    public StatusController(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }
    
    /**
     * GET /api/statuses - Get all statuses
     * 
     * @return List of all statuses
     */
    @GetMapping
    public ResponseEntity<List<Status>> getAllStatuses() {
        try {
            List<Status> statuses = statusRepository.findAll();
            logger.info("Retrieved {} statuses", statuses.size());
            return ResponseEntity.ok(statuses);
        } catch (Exception e) {
            logger.error("Failed to retrieve statuses", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to retrieve statuses", e);
        }
    }
    
    /**
     * POST /api/statuses - Create a new status
     * 
     * @param status Status to create
     * @return Created status
     */
    @PostMapping
    public ResponseEntity<Status> createStatus(@Valid @RequestBody Status status) {
        try {
            // Check for duplicate label
            Optional<Status> existing = statusRepository.findByLabel(status.getLabel());
            if (existing.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, 
                        "Status with label '" + status.getLabel() + "' already exists");
            }
            
            Status savedStatus = statusRepository.save(status);
            logger.info("Created status with ID: {}", savedStatus.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStatus);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to create status", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to create status", e);
        }
    }
    
    /**
     * PUT /api/statuses/{id} - Update a status
     * 
     * @param id Status ID
     * @param status Updated status data
     * @return Updated status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Status> updateStatus(@PathVariable String id, 
                                               @Valid @RequestBody Status status) {
        try {
            // Check if status exists
            Optional<Status> existing = statusRepository.findById(id);
            if (existing.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Status not found with ID: " + id);
            }
            
            // Check for duplicate label (excluding current status)
            Optional<Status> duplicate = statusRepository.findByLabel(status.getLabel());
            if (duplicate.isPresent() && !duplicate.get().getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, 
                        "Status with label '" + status.getLabel() + "' already exists");
            }
            
            // Set ID and update
            status.setId(id);
            Status updatedStatus = statusRepository.update(status);
            logger.info("Updated status with ID: {}", id);
            return ResponseEntity.ok(updatedStatus);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to update status", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to update status", e);
        }
    }
    
    /**
     * DELETE /api/statuses/{id} - Delete a status
     * 
     * @param id Status ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable String id) {
        try {
            boolean deleted = statusRepository.deleteById(id);
            if (deleted) {
                logger.info("Deleted status with ID: {}", id);
                return ResponseEntity.noContent().build();
            } else {
                logger.warn("Status not found with ID: {}", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Status not found with ID: " + id);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to delete status", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to delete status", e);
        }
    }
}
