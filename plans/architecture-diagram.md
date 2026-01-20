# Task Management System - Architecture Diagram

## System Overview

```mermaid
graph TB
    subgraph "Frontend - React SPA"
        A[Browser] --> B[App.jsx]
        B --> C[TaskList Component]
        B --> D[TaskForm Component]
        B --> E[StatusManager Component]
        B --> F[MessageBanner Component]
        C --> G[API Service Layer]
        D --> G
        E --> G
    end
    
    subgraph "Backend - Spring Boot API"
        G -->|HTTP/REST| H[TaskController]
        G -->|HTTP/REST| I[StatusController]
        H --> J[TaskRepository]
        I --> K[StatusRepository]
        J --> L[JsonFileRepository]
        K --> L
    end
    
    subgraph "Storage Layer"
        L -->|Read/Write with Lock| M[tasks.json]
        L -->|Read/Write with Lock| N[statuses.json]
    end
    
    style A fill:#e1f5ff
    style B fill:#fff4e1
    style G fill:#ffe1e1
    style H fill:#e1ffe1
    style I fill:#e1ffe1
    style L fill:#f0e1ff
    style M fill:#ffffcc
    style N fill:#ffffcc
```

## Component Interaction Flow

### Task Creation Flow

```mermaid
sequenceDiagram
    participant User
    participant TaskForm
    participant API
    participant Controller
    participant Repository
    participant FileSystem
    
    User->>TaskForm: Enter task text & select status
    TaskForm->>API: POST /api/tasks
    API->>Controller: createTask(request)
    Controller->>Repository: save(task)
    Repository->>FileSystem: Acquire file lock
    FileSystem-->>Repository: Lock acquired
    Repository->>FileSystem: Write to tasks.json
    FileSystem-->>Repository: Write successful
    Repository->>FileSystem: Release lock
    Repository-->>Controller: Task saved
    Controller-->>API: 201 Created + task
    API-->>TaskForm: Success response
    TaskForm-->>User: Display success message
    TaskForm->>TaskForm: Refresh task list
```

### Status Management Flow

```mermaid
sequenceDiagram
    participant User
    participant StatusManager
    participant API
    participant Controller
    participant Repository
    participant FileSystem
    
    User->>StatusManager: Create new status
    StatusManager->>API: POST /api/statuses
    API->>Controller: createStatus(request)
    Controller->>Repository: save(status)
    Repository->>FileSystem: Acquire file lock
    FileSystem-->>Repository: Lock acquired
    Repository->>FileSystem: Write to statuses.json
    FileSystem-->>Repository: Write successful
    Repository->>FileSystem: Release lock
    Repository-->>Controller: Status saved
    Controller-->>API: 201 Created + status
    API-->>StatusManager: Success response
    StatusManager-->>User: Display success message
    StatusManager->>StatusManager: Refresh status list
```

## Data Model Relationships

```mermaid
erDiagram
    TASK ||--o| STATUS : has
    TASK {
        string id PK
        string text
        datetime created
        string statusId FK
    }
    STATUS {
        string id PK
        string label
        string color
    }
```

## File Locking Mechanism

```mermaid
stateDiagram-v2
    [*] --> Idle
    Idle --> AcquiringLock: Write Request
    AcquiringLock --> Locked: Lock Acquired
    AcquiringLock --> Waiting: Lock Busy
    Waiting --> AcquiringLock: Retry
    Waiting --> Failed: Timeout
    Locked --> Writing: Perform Write
    Writing --> ReleasingLock: Write Complete
    ReleasingLock --> Idle: Lock Released
    Failed --> [*]: Return Error
```

## Deployment Architecture

```mermaid
graph LR
    subgraph "Development Environment"
        A[Developer Machine]
        B[Backend :8080]
        C[Frontend :5173]
        D[File Storage]
        
        A --> B
        A --> C
        C -->|API Calls| B
        B --> D
    end
    
    style A fill:#e1f5ff
    style B fill:#e1ffe1
    style C fill:#fff4e1
    style D fill:#ffffcc
```

## Technology Stack Layers

```mermaid
graph TD
    subgraph "Presentation Layer"
        A[React 18+]
        B[CSS3]
        C[Vite Dev Server]
    end
    
    subgraph "API Layer"
        D[Spring Boot 3.x]
        E[Spring Web]
        F[Jackson JSON]
    end
    
    subgraph "Business Logic"
        G[Controllers]
        H[Repositories]
        I[File Locking]
    end
    
    subgraph "Data Layer"
        J[JSON Files]
        K[File System]
    end
    
    A --> D
    B --> A
    C --> A
    D --> G
    E --> D
    F --> D
    G --> H
    H --> I
    I --> J
    J --> K
    
    style A fill:#61dafb
    style D fill:#6db33f
    style J fill:#f7df1e
```

## Error Handling Flow

```mermaid
graph TD
    A[User Action] --> B{API Call}
    B -->|Success| C[Update UI]
    B -->|Network Error| D[Display Error Message]
    B -->|Validation Error| E[Show Validation Feedback]
    B -->|Server Error| F[Display Generic Error]
    
    C --> G[Show Success Banner]
    D --> H[Retry Option]
    E --> I[Highlight Invalid Fields]
    F --> J[Log Error Details]
    
    G --> K[Auto-dismiss after 3s]
    H --> A
    I --> A
    J --> L[Return to Previous State]
    
    style A fill:#e1f5ff
    style C fill:#e1ffe1
    style D fill:#ffe1e1
    style E fill:#fff4e1
    style F fill:#ffe1e1
```

## State Management Pattern

```mermaid
graph LR
    A[Component Mount] --> B[Fetch Initial Data]
    B --> C[Update State]
    C --> D[Render UI]
    D --> E[User Interaction]
    E --> F[API Call]
    F --> G{Success?}
    G -->|Yes| H[Update State]
    G -->|No| I[Show Error]
    H --> D
    I --> D
    
    style A fill:#e1f5ff
    style C fill:#fff4e1
    style H fill:#fff4e1
    style D fill:#e1ffe1
```

---

## Key Design Decisions

### 1. Separation of Concerns
- **Frontend**: Pure presentation and user interaction
- **Backend**: Business logic and data persistence
- **Storage**: Simple JSON files with file locking

### 2. RESTful API Design
- Standard HTTP methods (GET, POST, PUT, DELETE)
- Proper status codes (200, 201, 204, 400, 404, 500)
- JSON request/response format

### 3. File Locking Strategy
- Exclusive locks for write operations
- Prevents concurrent write conflicts
- Timeout mechanism for deadlock prevention

### 4. Component Architecture
- Reusable React components
- Single responsibility principle
- Props-based communication

### 5. Error Handling
- Graceful degradation
- User-friendly error messages
- Automatic retry for transient failures

---

**Diagram Version**: 1.0  
**Last Updated**: 2026-01-20
