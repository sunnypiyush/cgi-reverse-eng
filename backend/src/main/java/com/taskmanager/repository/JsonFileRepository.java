package com.taskmanager.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic JSON file repository with file locking for concurrent access safety.
 * Provides thread-safe read/write operations to JSON files.
 */
@Component
public class JsonFileRepository<T> {
    
    private static final Logger logger = LoggerFactory.getLogger(JsonFileRepository.class);
    private static final int LOCK_TIMEOUT_MS = 5000;
    private static final int LOCK_RETRY_DELAY_MS = 100;
    
    private final ObjectMapper objectMapper;
    
    public JsonFileRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    /**
     * Read all entities from JSON file with file locking.
     * 
     * @param filePath Path to the JSON file
     * @param typeReference TypeReference for deserialization
     * @return List of entities
     * @throws IOException if file operations fail
     */
    public List<T> readAll(String filePath, TypeReference<List<T>> typeReference) throws IOException {
        Path path = Paths.get(filePath);
        
        // If file doesn't exist, return empty list
        if (!Files.exists(path)) {
            logger.info("File does not exist, returning empty list: {}", filePath);
            return new ArrayList<>();
        }
        
        File file = path.toFile();
        
        // If file is empty, return empty list
        if (file.length() == 0) {
            logger.info("File is empty, returning empty list: {}", filePath);
            return new ArrayList<>();
        }
        
        try (RandomAccessFile raf = new RandomAccessFile(file, "r");
             FileChannel channel = raf.getChannel()) {
            
            // Acquire shared lock for reading
            FileLock lock = acquireLock(channel, true);
            
            try {
                byte[] data = new byte[(int) raf.length()];
                raf.readFully(data);
                String content = new String(data);
                
                // Validate JSON structure
                if (content.trim().isEmpty() || content.trim().equals("[]")) {
                    return new ArrayList<>();
                }
                
                return objectMapper.readValue(content, typeReference);
            } catch (Exception e) {
                logger.error("Error reading JSON from file: {}", filePath, e);
                throw new IOException("Failed to read JSON file: " + filePath, e);
            } finally {
                if (lock != null) {
                    lock.release();
                }
            }
        }
    }
    
    /**
     * Write all entities to JSON file with file locking.
     * 
     * @param filePath Path to the JSON file
     * @param entities List of entities to write
     * @throws IOException if file operations fail
     */
    public void writeAll(String filePath, List<T> entities) throws IOException {
        Path path = Paths.get(filePath);
        
        // Create parent directories if they don't exist
        Files.createDirectories(path.getParent());
        
        // Create file if it doesn't exist
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        
        File file = path.toFile();
        
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel channel = raf.getChannel()) {
            
            // Acquire exclusive lock for writing
            FileLock lock = acquireLock(channel, false);
            
            try {
                // Serialize to JSON
                String json = objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(entities);
                
                // Clear file and write new content
                raf.setLength(0);
                raf.write(json.getBytes());
                
                logger.debug("Successfully wrote {} entities to file: {}", entities.size(), filePath);
            } catch (Exception e) {
                logger.error("Error writing JSON to file: {}", filePath, e);
                throw new IOException("Failed to write JSON file: " + filePath, e);
            } finally {
                if (lock != null) {
                    lock.release();
                }
            }
        }
    }
    
    /**
     * Acquire file lock with retry logic and timeout.
     * 
     * @param channel FileChannel to lock
     * @param shared true for shared lock (read), false for exclusive lock (write)
     * @return FileLock instance
     * @throws IOException if lock cannot be acquired within timeout
     */
    private FileLock acquireLock(FileChannel channel, boolean shared) throws IOException {
        long startTime = System.currentTimeMillis();
        
        while (System.currentTimeMillis() - startTime < LOCK_TIMEOUT_MS) {
            try {
                FileLock lock = channel.tryLock(0, Long.MAX_VALUE, shared);
                if (lock != null) {
                    return lock;
                }
            } catch (IOException e) {
                // Lock acquisition failed, will retry
            }
            
            // Wait before retrying
            try {
                Thread.sleep(LOCK_RETRY_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Lock acquisition interrupted", e);
            }
        }
        
        throw new IOException("Failed to acquire file lock within timeout");
    }
}
