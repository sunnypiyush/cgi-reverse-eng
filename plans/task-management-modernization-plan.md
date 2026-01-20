# Task Management System Modernization Plan

**Feature**: Modernize Perl CGI Task Manager to Java + React  
**Branch**: `001-task-management-system`  
**Type**: Prototype  
**Created**: 2026-01-20

---

## Executive Summary

Modernize the existing Perl CGI task management application into a Java Spring Boot REST API backend with React frontend. The prototype maintains the simplicity of JSON file storage while adding custom status management and improving architecture.

### Key Changes from Legacy System
- **Architecture**: Monolithic CGI → Separate frontend/backend with REST API
- **Backend**: Perl → Java Spring Boot
- **Frontend**: Server-rendered HTML → React SPA
- **New Feature**: Custom user-defined task statuses
- **Improved**: File locking for concurrent access safety
- **Maintained**: JSON file storage, single-user model, no task editing

---

## Technical Context

### Technology Stack
- **Backend**: Java 17+, Spring Boot 3.x, Jackson (JSON), Spring Web
- **Frontend**: React 18+, Axios (HTTP client), CSS3
- **Storage**: JSON files (tasks.json, statuses.json)
- **Build Tools**: Maven (backend), npm/Vite (frontend)
- **Development**: Local development servers (Spring Boot embedded Tomcat, Vite dev server)

### Architecture Pattern
```
┌─────────────────┐         HTTP/REST          ┌──────────────────┐
│  React Frontend │ ◄────────────────────────► │ Spring Boot API  │
│  (Port 5173)    │      JSON Requests         │  (Port 8080)     │
└─────────────────┘                             └──────────────────┘
                                                         │
                                                         ▼
                                                ┌─────────────────┐
                                                │  File Storage   │
                                                │  tasks.json     │
                                                │  statuses.json  │
                                                └─────────────────┘
```

### Data Model

**Task Entity**
```json
{
  "id": "1737345600000-a1b2c3d4",
  "text": "Review documentation",
  "created": "2026-01-20T08:00:00Z",
  "statusId": "status-001"
}
```

**Status Entity**
```json
{
  "id": "status-001",
  "label": "To Do",
  "color": "#4a90e2"
}
```

---

## Constitution Check

### Alignment with Core Principles

✅ **Prototype Only**: This plan focuses on quick prototyping with local validation
- Simple JSON file storage (no database complexity)
- Embedded servers for easy local testing
- Minimal dependencies and configuration
- No authentication or deployment concerns

✅ **Quick Validation**: Each component can be tested independently
- Backend API can be tested with curl/Postman
- Frontend can be developed with mock data
- File storage can be validated with direct JSON inspection

### Potential Violations
None identified. The plan aligns with prototype-first principles.

---

## Implementation Plan

### Phase 1: Backend Foundation

#### Step 1: Project Setup
**Goal**: Initialize Spring Boot project with required dependencies

**Actions**:
- Create Maven project with Spring Boot starter (web, devtools)
- Add Jackson for JSON processing
- Configure application.properties (port, CORS, file paths)
- Create package structure: `model`, `repository`, `service`, `controller`

**Deliverables**:
- `pom.xml` with dependencies
- `application.properties` configured
- Basic project structure

**Validation**: `mvn spring-boot:run` starts successfully on port 8080

---

#### Step 2: Data Models
**Goal**: Define Task and Status entities with JSON serialization

**Actions**:
- Create `Task.java` class with fields: id, text, created, statusId
- Create `Status.java` class with fields: id, label, color
- Add Jackson annotations for JSON mapping
- Add validation annotations (NotNull, Size)

**Deliverables**:
- `model/Task.java`
- `model/Status.java`

**Validation**: Objects serialize/deserialize to JSON correctly

---

#### Step 3: File Storage Layer
**Goal**: Implement JSON file persistence with file locking

**Actions**:
- Create `JsonFileRepository.java` with generic read/write methods
- Implement file locking using `FileChannel.lock()`
- Add error handling for missing/corrupted files
- Create default statuses on first run ("To Do", "In Progress", "Done")

**Deliverables**:
- `repository/JsonFileRepository.java`
- `repository/TaskRepository.java`
- `repository/StatusRepository.java`

**Validation**: 
- Write and read tasks/statuses from JSON files
- Concurrent writes don't corrupt data
- Missing files initialize with defaults

---

#### Step 4: REST API Endpoints
**Goal**: Expose CRUD operations via REST API

**Actions**:
- Create `TaskController.java` with endpoints:
  - `GET /api/tasks` - List all tasks
  - `POST /api/tasks` - Create task
  - `DELETE /api/tasks/{id}` - Delete task
  - `DELETE /api/tasks` - Clear all tasks
  - `POST /api/tasks/reload` - Reload from file
- Create `StatusController.java` with endpoints:
  - `GET /api/statuses` - List all statuses
  - `POST /api/statuses` - Create status
  - `PUT /api/statuses/{id}` - Update status
  - `DELETE /api/statuses/{id}` - Delete status
- Add CORS configuration for frontend access
- Implement input validation and error responses

**Deliverables**:
- `controller/TaskController.java`
- `controller/StatusController.java`
- `config/CorsConfig.java`

**Validation**: 
- Test all endpoints with curl/Postman
- Verify CORS headers present
- Confirm error handling works

---

### Phase 2: Frontend Development

#### Step 5: React Project Setup
**Goal**: Initialize React application with Vite

**Actions**:
- Create React project with Vite: `npm create vite@latest frontend -- --template react`
- Install Axios for API calls: `npm install axios`
- Configure proxy to backend (port 8080)
- Create component structure: `components/`, `services/`, `styles/`

**Deliverables**:
- React project in `frontend/` directory
- `package.json` with dependencies
- Basic folder structure

**Validation**: `npm run dev` starts development server on port 5173

---

#### Step 6: API Service Layer
**Goal**: Create service layer for backend communication

**Actions**:
- Create `services/api.js` with Axios instance
- Implement task API methods: `getTasks()`, `createTask()`, `deleteTask()`, `clearTasks()`, `reloadTasks()`
- Implement status API methods: `getStatuses()`, `createStatus()`, `updateStatus()`, `deleteStatus()`
- Add error handling and response parsing

**Deliverables**:
- `services/api.js`

**Validation**: API calls return expected data structure

---

#### Step 7: React Components
**Goal**: Build UI components for task and status management

**Actions**:
- Create `TaskList.jsx` - Display tasks in table with delete buttons
- Create `TaskForm.jsx` - Form to add new tasks with status dropdown
- Create `StatusManager.jsx` - Manage custom statuses (create, edit, delete)
- Create `MessageBanner.jsx` - Display success/error messages
- Create `App.jsx` - Main component orchestrating all parts
- Add state management with React hooks (useState, useEffect)

**Deliverables**:
- `components/TaskList.jsx`
- `components/TaskForm.jsx`
- `components/StatusManager.jsx`
- `components/MessageBanner.jsx`
- `App.jsx`

**Validation**: Components render and handle user interactions

---

#### Step 8: Styling and Polish
**Goal**: Apply styling similar to original CGI app

**Actions**:
- Create `styles/App.css` with:
  - Brown header bar (#5c4033)
  - Color-coded buttons (blue add, red delete, orange clear, cyan reload)
  - Table styling with alternating rows
  - Status badges with color indicators
  - Responsive layout (max-width 1200px)
- Add loading states and transitions
- Implement success/error message display (3-second auto-dismiss)

**Deliverables**:
- `styles/App.css`
- Styled components

**Validation**: UI matches original design aesthetic and is responsive

---

### Phase 3: Integration and Testing

#### Step 9: End-to-End Integration
**Goal**: Connect frontend and backend, test complete workflows

**Actions**:
- Start both backend (port 8080) and frontend (port 5173)
- Test complete user flows:
  - Create task with status → verify in JSON file
  - Delete task → verify removal
  - Clear all tasks → verify empty state
  - Reload tasks → verify refresh
  - Create custom status → verify availability
  - Edit status → verify label update
  - Delete status → verify handling
- Test error scenarios (backend down, invalid input)
- Test with sample data from original tasks.json

**Deliverables**:
- Working prototype with both servers running
- Test results documented

**Validation**: All user stories from spec work correctly

---

## Project Structure

```
cgi-reverse-eng/
├── backend/                    # Java Spring Boot API
│   ├── src/main/java/com/taskmanager/
│   │   ├── model/
│   │   │   ├── Task.java
│   │   │   └── Status.java
│   │   ├── repository/
│   │   │   ├── JsonFileRepository.java
│   │   │   ├── TaskRepository.java
│   │   │   └── StatusRepository.java
│   │   ├── controller/
│   │   │   ├── TaskController.java
│   │   │   └── StatusController.java
│   │   ├── config/
│   │   │   └── CorsConfig.java
│   │   └── TaskManagerApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── pom.xml
│   └── data/                   # JSON storage
│       ├── tasks.json
│       └── statuses.json
│
├── frontend/                   # React SPA
│   ├── src/
│   │   ├── components/
│   │   │   ├── TaskList.jsx
│   │   │   ├── TaskForm.jsx
│   │   │   ├── StatusManager.jsx
│   │   │   └── MessageBanner.jsx
│   │   ├── services/
│   │   │   └── api.js
│   │   ├── styles/
│   │   │   └── App.css
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── package.json
│   └── vite.config.js
│
└── plans/
    └── task-management-modernization-plan.md
```

---

## API Contract

### Task Endpoints

**GET /api/tasks**
- Response: `200 OK` with array of tasks
```json
[
  {
    "id": "1737345600000-a1b2c3d4",
    "text": "Review documentation",
    "created": "2026-01-20T08:00:00Z",
    "statusId": "status-001"
  }
]
```

**POST /api/tasks**
- Request body:
```json
{
  "text": "New task description",
  "statusId": "status-001"
}
```
- Response: `201 Created` with created task

**DELETE /api/tasks/{id}**
- Response: `204 No Content`

**DELETE /api/tasks**
- Response: `204 No Content` (clears all)

**POST /api/tasks/reload**
- Response: `200 OK` with reloaded tasks array

### Status Endpoints

**GET /api/statuses**
- Response: `200 OK` with array of statuses

**POST /api/statuses**
- Request body:
```json
{
  "label": "Blocked",
  "color": "#ff6b6b"
}
```
- Response: `201 Created` with created status

**PUT /api/statuses/{id}**
- Request body: Same as POST
- Response: `200 OK` with updated status

**DELETE /api/statuses/{id}**
- Response: `204 No Content`

---

## Key Implementation Details

### File Locking Strategy
```java
// Acquire exclusive lock before write operations
try (FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE)) {
    FileLock lock = channel.lock();
    // Write operation
    lock.release();
}
```

### ID Generation
- Tasks: `timestamp + "-" + UUID.randomUUID().toString().substring(0, 8)`
- Statuses: `"status-" + UUID.randomUUID().toString().substring(0, 8)`

### Error Handling
- Backend: Return appropriate HTTP status codes with error messages
- Frontend: Display user-friendly error messages in MessageBanner
- File operations: Log errors, attempt recovery, initialize fresh storage if needed

### Input Validation
- Task text: 1-500 characters, HTML sanitized
- Status label: 1-50 characters, no duplicates
- All IDs: Non-empty, valid format

---

## Testing Strategy

### Backend Testing
1. **Unit Tests**: Test repository methods with temporary files
2. **API Tests**: Test endpoints with MockMvc
3. **Concurrency Tests**: Simulate concurrent writes to verify file locking

### Frontend Testing
1. **Component Tests**: Test individual components with mock data
2. **Integration Tests**: Test API service layer with mock backend
3. **Manual Tests**: Test complete workflows in browser

### Prototype Validation
- Create 10 tasks with different statuses
- Delete individual tasks
- Clear all tasks
- Reload tasks from file
- Create/edit/delete custom statuses
- Test with original tasks.json data
- Verify JSON files are properly formatted
- Test concurrent operations (open multiple browser tabs)

---

## Development Workflow

### Running the Prototype

**Backend**:
```bash
cd backend
mvn spring-boot:run
# Server starts on http://localhost:8080
```

**Frontend**:
```bash
cd frontend
npm install
npm run dev
# Dev server starts on http://localhost:5173
```

**Access**: Open browser to `http://localhost:5173`

### Quick Validation Checklist
- [ ] Backend starts without errors
- [ ] Frontend connects to backend
- [ ] Can create task with status
- [ ] Can delete task
- [ ] Can clear all tasks
- [ ] Can reload tasks
- [ ] Can create custom status
- [ ] Can edit status label
- [ ] Can delete status
- [ ] JSON files update correctly
- [ ] UI displays success/error messages
- [ ] Styling matches original aesthetic

---

## Migration from Legacy System

### Data Migration
The existing `tasks.json` needs status IDs added:

**Before** (legacy format):
```json
[
  {
    "id": "1733587200123",
    "text": "Review project documentation",
    "created": "Fri Dec  6 10:30:00 2024"
  }
]
```

**After** (new format):
```json
[
  {
    "id": "1733587200123-migrated",
    "text": "Review project documentation",
    "created": "2024-12-06T15:30:00Z",
    "statusId": "status-default"
  }
]
```

**Migration Script**: Create a one-time migration utility to:
1. Read legacy tasks.json
2. Add default statusId to each task
3. Convert timestamp format to ISO 8601
4. Append "-migrated" to IDs to avoid collisions
5. Write to new format

---

## Success Metrics

### Functional Completeness
- ✅ All CRUD operations work for tasks
- ✅ All CRUD operations work for statuses
- ✅ File locking prevents data corruption
- ✅ UI provides immediate feedback
- ✅ Error handling works gracefully

### Performance Targets
- Task creation: < 1 second (including persistence)
- Page load with 100 tasks: < 2 seconds
- API response time: < 200ms (excluding file I/O)

### Code Quality
- Clean separation of concerns (model, repository, controller, service)
- Consistent error handling patterns
- Input validation on all endpoints
- XSS prevention through sanitization
- Proper HTTP status codes

---

## Known Limitations (Prototype)

### Not Implemented
- ❌ Unit tests (prototype focuses on manual validation)
- ❌ Production deployment configuration
- ❌ Database backend option
- ❌ Task editing (by design - must delete and recreate)
- ❌ Search/filter functionality
- ❌ Pagination (all tasks on one page)
- ❌ User authentication
- ❌ CSRF tokens (single-user prototype)

### Future Enhancements (Post-Prototype)
- Add comprehensive test suite
- Implement task search and filtering
- Add pagination for large task lists
- Support task editing
- Add task categories/tags
- Implement due dates
- Add export functionality (CSV/PDF)
- Consider database backend for scalability

---

## Risk Assessment

### Technical Risks
| Risk | Impact | Mitigation |
|------|--------|------------|
| File locking fails on Windows | High | Test on Windows, implement retry logic |
| CORS issues in production | Medium | Configure CORS properly, test cross-origin |
| Large task lists slow UI | Low | Implement pagination if needed |
| JSON corruption | Medium | Backup before write, validate on read |

### Prototype Risks
| Risk | Impact | Mitigation |
|------|--------|------------|
| Scope creep beyond prototype | High | Stick to core features only |
| Over-engineering | Medium | Use simplest solutions, avoid premature optimization |
| Testing gaps | Low | Focus on manual validation for prototype |

---

## Next Steps

1. **Review this plan** with stakeholders
2. **Confirm technology choices** (Java 17+, React 18+, Spring Boot 3.x)
3. **Set up development environment** (JDK, Node.js, Maven, npm)
4. **Begin implementation** following the step-by-step plan
5. **Validate each step** before proceeding to next
6. **Document issues** encountered during development
7. **Demo prototype** with sample data

---

## Appendix: Command Reference

### Backend Commands
```bash
# Create Spring Boot project
mvn archetype:generate -DgroupId=com.taskmanager \
  -DartifactId=backend -DarchetypeArtifactId=maven-archetype-quickstart

# Run backend
mvn spring-boot:run

# Build JAR
mvn clean package

# Run tests
mvn test
```

### Frontend Commands
```bash
# Create React project
npm create vite@latest frontend -- --template react

# Install dependencies
npm install

# Run dev server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

### Testing Commands
```bash
# Test backend API
curl http://localhost:8080/api/tasks
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"text":"Test task","statusId":"status-001"}'

# Check JSON files
cat backend/data/tasks.json | jq .
cat backend/data/statuses.json | jq .
```

---

**Plan Status**: Ready for Implementation  
**Estimated Prototype Completion**: 1-2 days of focused development  
**Last Updated**: 2026-01-20
