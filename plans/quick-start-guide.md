# Quick Start Implementation Guide

This guide provides the essential code snippets and commands to build the prototype quickly.

---

## 1. Backend Setup (Java Spring Boot)

### Create Project Structure
```bash
mkdir -p backend/src/main/java/com/taskmanager/{model,repository,controller,config}
mkdir -p backend/src/main/resources
mkdir -p backend/data
cd backend
```

### pom.xml (Essential Dependencies)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>
    
    <groupId>com.taskmanager</groupId>
    <artifactId>backend</artifactId>
    <version>1.0.0</version>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### application.properties
```properties
server.port=8080
spring.application.name=task-manager
storage.tasks.path=data/tasks.json
storage.statuses.path=data/statuses.json
```

### Task.java (Model)
```java
package com.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Task {
    @JsonProperty("id")
    private String id;
    
    @NotBlank
    @Size(min = 1, max = 500)
    @JsonProperty("text")
    private String text;
    
    @JsonProperty("created")
    private String created;
    
    @JsonProperty("statusId")
    private String statusId;
    
    // Constructors
    public Task() {}
    
    public Task(String id, String text, String created, String statusId) {
        this.id = id;
        this.text = text;
        this.created = created;
        this.statusId = statusId;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }
    
    public String getStatusId() { return statusId; }
    public void setStatusId(String statusId) { this.statusId = statusId; }
}
```

### Status.java (Model)
```java
package com.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Status {
    @JsonProperty("id")
    private String id;
    
    @NotBlank
    @Size(min = 1, max = 50)
    @JsonProperty("label")
    private String label;
    
    @JsonProperty("color")
    private String color;
    
    // Constructors
    public Status() {}
    
    public Status(String id, String label, String color) {
        this.id = id;
        this.label = label;
        this.color = color;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
```

### JsonFileRepository.java (Core Storage Logic)
```java
package com.taskmanager.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JsonFileRepository<T> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public List<T> readFromFile(String filePath, TypeReference<List<T>> typeRef) {
        Path path = Paths.get(filePath);
        
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        
        try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "r");
             FileChannel channel = file.getChannel();
             FileLock lock = channel.lock(0, Long.MAX_VALUE, true)) {
            
            String content = new String(Files.readAllBytes(path));
            if (content.trim().isEmpty()) {
                return new ArrayList<>();
            }
            
            return objectMapper.readValue(content, typeRef);
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public void writeToFile(String filePath, List<T> data) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        
        try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "rw");
             FileChannel channel = file.getChannel();
             FileLock lock = channel.lock()) {
            
            String json = objectMapper.writerWithDefaultPrettyPrinter()
                                     .writeValueAsString(data);
            
            file.setLength(0);
            file.write(json.getBytes());
            
        }
    }
}
```

### TaskController.java (REST API)
```java
package com.taskmanager.controller;

import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskRepository.findAll());
    }
    
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        String id = System.currentTimeMillis() + "-" + 
                    UUID.randomUUID().toString().substring(0, 8);
        task.setId(id);
        task.setCreated(Instant.now().toString());
        
        taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping
    public ResponseEntity<Void> clearAllTasks() {
        taskRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/reload")
    public ResponseEntity<List<Task>> reloadTasks() {
        return ResponseEntity.ok(taskRepository.findAll());
    }
}
```

### TaskRepository.java
```java
package com.taskmanager.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.taskmanager.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TaskRepository {
    
    @Autowired
    private JsonFileRepository<Task> fileRepository;
    
    @Value("${storage.tasks.path}")
    private String filePath;
    
    public List<Task> findAll() {
        return fileRepository.readFromFile(filePath, new TypeReference<List<Task>>() {});
    }
    
    public void save(Task task) {
        List<Task> tasks = findAll();
        tasks.add(task);
        try {
            fileRepository.writeToFile(filePath, tasks);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save task", e);
        }
    }
    
    public void deleteById(String id) {
        List<Task> tasks = findAll();
        tasks = tasks.stream()
                    .filter(t -> !t.getId().equals(id))
                    .collect(Collectors.toList());
        try {
            fileRepository.writeToFile(filePath, tasks);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete task", e);
        }
    }
    
    public void deleteAll() {
        try {
            fileRepository.writeToFile(filePath, List.of());
        } catch (IOException e) {
            throw new RuntimeException("Failed to clear tasks", e);
        }
    }
}
```

### Run Backend
```bash
mvn spring-boot:run
```

---

## 2. Frontend Setup (React + Vite)

### Create Project
```bash
npm create vite@latest frontend -- --template react
cd frontend
npm install
npm install axios
```

### vite.config.js (Proxy Configuration)
```javascript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

### src/services/api.js
```javascript
import axios from 'axios';

const API_BASE_URL = '/api';

export const taskAPI = {
  getTasks: () => axios.get(`${API_BASE_URL}/tasks`),
  createTask: (task) => axios.post(`${API_BASE_URL}/tasks`, task),
  deleteTask: (id) => axios.delete(`${API_BASE_URL}/tasks/${id}`),
  clearTasks: () => axios.delete(`${API_BASE_URL}/tasks`),
  reloadTasks: () => axios.post(`${API_BASE_URL}/tasks/reload`)
};

export const statusAPI = {
  getStatuses: () => axios.get(`${API_BASE_URL}/statuses`),
  createStatus: (status) => axios.post(`${API_BASE_URL}/statuses`, status),
  updateStatus: (id, status) => axios.put(`${API_BASE_URL}/statuses/${id}`, status),
  deleteStatus: (id) => axios.delete(`${API_BASE_URL}/statuses/${id}`)
};
```

### src/components/TaskForm.jsx
```javascript
import { useState } from 'react';

function TaskForm({ statuses, onTaskCreated }) {
  const [text, setText] = useState('');
  const [statusId, setStatusId] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!text.trim()) return;
    
    try {
      await onTaskCreated({ text, statusId: statusId || statuses[0]?.id });
      setText('');
    } catch (error) {
      console.error('Error creating task:', error);
    }
  };

  return (
    <div className="form-section">
      <h3>Add New Task</h3>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={text}
          onChange={(e) => setText(e.target.value)}
          placeholder="Enter task description..."
          required
          maxLength={500}
        />
        <select 
          value={statusId} 
          onChange={(e) => setStatusId(e.target.value)}
        >
          {statuses.map(status => (
            <option key={status.id} value={status.id}>
              {status.label}
            </option>
          ))}
        </select>
        <button type="submit">Add Task</button>
      </form>
    </div>
  );
}

export default TaskForm;
```

### src/components/TaskList.jsx
```javascript
function TaskList({ tasks, statuses, onDeleteTask }) {
  const getStatusLabel = (statusId) => {
    const status = statuses.find(s => s.id === statusId);
    return status ? status.label : 'Unknown';
  };

  const getStatusColor = (statusId) => {
    const status = statuses.find(s => s.id === statusId);
    return status ? status.color : '#999';
  };

  return (
    <table className="data-table">
      <thead>
        <tr>
          <th>Task ID</th>
          <th>Description</th>
          <th>Created</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {tasks.length === 0 ? (
          <tr>
            <td colSpan="5" className="no-records">No Records Found</td>
          </tr>
        ) : (
          tasks.map(task => (
            <tr key={task.id}>
              <td>{task.id}</td>
              <td>{task.text}</td>
              <td>{new Date(task.created).toLocaleString()}</td>
              <td>
                <span 
                  className="status-badge" 
                  style={{ backgroundColor: getStatusColor(task.statusId) }}
                >
                  {getStatusLabel(task.statusId)}
                </span>
              </td>
              <td>
                <button 
                  className="delete" 
                  onClick={() => onDeleteTask(task.id)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))
        )}
      </tbody>
    </table>
  );
}

export default TaskList;
```

### src/App.jsx (Main Component)
```javascript
import { useState, useEffect } from 'react';
import { taskAPI, statusAPI } from './services/api';
import TaskForm from './components/TaskForm';
import TaskList from './components/TaskList';
import './styles/App.css';

function App() {
  const [tasks, setTasks] = useState([]);
  const [statuses, setStatuses] = useState([]);
  const [message, setMessage] = useState('');

  useEffect(() => {
    loadTasks();
    loadStatuses();
  }, []);

  const loadTasks = async () => {
    try {
      const response = await taskAPI.getTasks();
      setTasks(response.data);
    } catch (error) {
      showMessage('Error loading tasks', 'error');
    }
  };

  const loadStatuses = async () => {
    try {
      const response = await statusAPI.getStatuses();
      setStatuses(response.data);
    } catch (error) {
      showMessage('Error loading statuses', 'error');
    }
  };

  const handleTaskCreated = async (task) => {
    try {
      await taskAPI.createTask(task);
      await loadTasks();
      showMessage('Task added successfully!');
    } catch (error) {
      showMessage('Error creating task', 'error');
    }
  };

  const handleDeleteTask = async (id) => {
    try {
      await taskAPI.deleteTask(id);
      await loadTasks();
      showMessage('Task deleted successfully!');
    } catch (error) {
      showMessage('Error deleting task', 'error');
    }
  };

  const handleClearAll = async () => {
    if (!window.confirm('Are you sure you want to clear all tasks?')) return;
    
    try {
      await taskAPI.clearTasks();
      await loadTasks();
      showMessage('All tasks cleared!');
    } catch (error) {
      showMessage('Error clearing tasks', 'error');
    }
  };

  const showMessage = (text, type = 'success') => {
    setMessage({ text, type });
    setTimeout(() => setMessage(''), 3000);
  };

  return (
    <div>
      <div className="header-bar"></div>
      <h1>Task Management System</h1>
      
      <div className="container">
        {message && (
          <div className={`message ${message.type}`}>
            {message.text}
          </div>
        )}
        
        <TaskForm 
          statuses={statuses} 
          onTaskCreated={handleTaskCreated} 
        />
        
        <TaskList 
          tasks={tasks} 
          statuses={statuses}
          onDeleteTask={handleDeleteTask} 
        />
        
        {tasks.length > 0 && (
          <div className="form-section">
            <button className="clear" onClick={handleClearAll}>
              Clear All Tasks
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
```

### src/styles/App.css (Essential Styles)
```css
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: Arial, sans-serif;
  background-color: #f5f5f5;
  padding: 20px;
}

.header-bar {
  background-color: #5c4033;
  height: 30px;
  margin-bottom: 20px;
}

h1 {
  text-align: center;
  font-size: 28px;
  margin-bottom: 30px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  background-color: white;
  padding: 20px;
}

.message {
  padding: 15px;
  margin-bottom: 20px;
  border: 2px solid #333;
  background-color: #d4edda;
  color: #155724;
  font-weight: bold;
}

.message.error {
  background-color: #f8d7da;
  color: #721c24;
}

.form-section {
  padding: 20px;
  background-color: #fff;
  border: 2px solid #333;
  margin-bottom: 20px;
}

input[type="text"], select {
  padding: 8px;
  border: 2px solid #999;
  font-size: 14px;
  margin-right: 10px;
}

input[type="text"] {
  width: 50%;
}

button {
  padding: 8px 20px;
  background-color: #4a90e2;
  color: white;
  border: 2px solid #333;
  font-weight: bold;
  cursor: pointer;
}

button:hover {
  background-color: #357abd;
}

button.delete {
  background-color: #d9534f;
  padding: 5px 10px;
  font-size: 12px;
}

button.clear {
  background-color: #f0ad4e;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  border: 2px solid #333;
  margin-top: 20px;
}

.data-table th {
  background-color: #d9d9d9;
  border: 1px solid #333;
  padding: 10px;
  text-align: left;
}

.data-table td {
  border: 1px solid #333;
  padding: 10px;
}

.data-table tr:nth-child(even) td {
  background-color: #f9f9f9;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 4px;
  color: white;
  font-size: 12px;
  font-weight: bold;
}

.no-records {
  text-align: center;
  padding: 20px;
}
```

### Run Frontend
```bash
npm run dev
```

---

## 3. Initialize Default Data

### backend/data/statuses.json
```json
[
  {
    "id": "status-001",
    "label": "To Do",
    "color": "#4a90e2"
  },
  {
    "id": "status-002",
    "label": "In Progress",
    "color": "#f0ad4e"
  },
  {
    "id": "status-003",
    "label": "Done",
    "color": "#5cb85c"
  }
]
```

### backend/data/tasks.json
```json
[]
```

---

## 4. Testing Commands

### Test Backend API
```bash
# Get all tasks
curl http://localhost:8080/api/tasks

# Create task
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"text":"Test task","statusId":"status-001"}'

# Delete task
curl -X DELETE http://localhost:8080/api/tasks/TASK_ID

# Get statuses
curl http://localhost:8080/api/statuses
```

### Access Application
Open browser to: `http://localhost:5173`

---

## 5. Troubleshooting

### Backend won't start
- Check Java version: `java -version` (need 17+)
- Check port 8080 is free: `lsof -i :8080`
- Check Maven: `mvn -version`

### Frontend won't connect
- Check backend is running on port 8080
- Check CORS configuration in controller
- Check proxy in vite.config.js

### File locking errors
- Ensure data directory exists
- Check file permissions
- Close any programs accessing JSON files

---

**Quick Start Version**: 1.0  
**Last Updated**: 2026-01-20
