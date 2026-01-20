# Tasks: Task Management System Modernization

**Input**: Design documents from `/specs/001-task-management-system/`
**Prerequisites**: plan.md (required), spec.md (required for user stories)

**Tests**: Tests are NOT included in this prototype - focus is on manual validation per constitution

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3, US4)
- Include exact file paths in descriptions

## Path Conventions

- **Web app structure**: `backend/src/`, `frontend/src/`
- Backend: Java Spring Boot with Maven
- Frontend: React with Vite

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [X] T001 Create backend directory structure: backend/src/main/java/com/taskmanager/{model,repository,controller,config}
- [X] T002 Create backend/pom.xml with Spring Boot 3.x dependencies (web, devtools, jackson)
- [X] T003 Create backend/src/main/resources/application.properties with port 8080, CORS, file paths
- [X] T004 [P] Create frontend directory with Vite: npm create vite@latest frontend -- --template react
- [X] T005 [P] Install frontend dependencies: axios for API calls
- [X] T006 [P] Create frontend/vite.config.js with proxy to backend port 8080
- [X] T007 [P] Create frontend directory structure: src/{components,services,styles}
- [X] T008 [P] Create backend/data directory for JSON storage
- [X] T009 [P] Initialize backend/data/statuses.json with default statuses (To Do, In Progress, Done)
- [X] T010 [P] Initialize backend/data/tasks.json as empty array

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T011 [P] Create Task model in backend/src/main/java/com/taskmanager/model/Task.java with fields: id, text, created, statusId
- [X] T012 [P] Create Status model in backend/src/main/java/com/taskmanager/model/Status.java with fields: id, label, color
- [X] T013 Create JsonFileRepository in backend/src/main/java/com/taskmanager/repository/JsonFileRepository.java with file locking
- [X] T014 Create TaskRepository in backend/src/main/java/com/taskmanager/repository/TaskRepository.java using JsonFileRepository
- [X] T015 Create StatusRepository in backend/src/main/java/com/taskmanager/repository/StatusRepository.java using JsonFileRepository
- [X] T016 Create TaskManagerApplication main class in backend/src/main/java/com/taskmanager/TaskManagerApplication.java
- [X] T017 [P] Create CorsConfig in backend/src/main/java/com/taskmanager/config/CorsConfig.java for frontend access
- [X] T018 [P] Create API service layer in frontend/src/services/api.js with Axios instance and base URL

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Basic Task Management (Priority: P1) 🎯 MVP

**Goal**: Users can create, view, and delete tasks to track their personal work items

**Independent Test**: Create a task, view it in the list, delete it. Verify JSON file updates correctly.

### Implementation for User Story 1

- [X] T019 [P] [US1] Create TaskController in backend/src/main/java/com/taskmanager/controller/TaskController.java with GET /api/tasks endpoint
- [X] T020 [P] [US1] Add POST /api/tasks endpoint to TaskController for creating tasks
- [X] T021 [P] [US1] Add DELETE /api/tasks/{id} endpoint to TaskController for deleting individual tasks
- [X] T022 [P] [US1] Add DELETE /api/tasks endpoint to TaskController for clearing all tasks
- [X] T023 [P] [US1] Add POST /api/tasks/reload endpoint to TaskController for reloading from file
- [X] T024 [P] [US1] Implement task API methods in frontend/src/services/api.js: getTasks, createTask, deleteTask, clearTasks, reloadTasks
- [X] T025 [US1] Create TaskList component in frontend/src/components/TaskList.jsx to display tasks in table
- [X] T026 [US1] Create TaskForm component in frontend/src/components/TaskForm.jsx with input and submit button
- [X] T027 [US1] Create MessageBanner component in frontend/src/components/MessageBanner.jsx for success/error messages
- [X] T028 [US1] Create App.jsx in frontend/src/App.jsx integrating TaskList, TaskForm, MessageBanner with state management
- [X] T029 [US1] Add task creation handler in App.jsx calling createTask API and refreshing list
- [X] T030 [US1] Add task deletion handler in App.jsx calling deleteTask API and refreshing list
- [X] T031 [US1] Add clear all handler in App.jsx with confirmation dialog calling clearTasks API
- [X] T032 [US1] Add reload handler in App.jsx calling reloadTasks API
- [X] T033 [US1] Add input validation in TaskForm: required field, max 500 characters
- [X] T034 [US1] Add error handling in App.jsx to display errors in MessageBanner
- [ ] T035 [US1] Test: Create task "Review documentation" → verify appears in list and in backend/data/tasks.json
- [ ] T036 [US1] Test: Delete task → verify removed from list and JSON file
- [ ] T037 [US1] Test: Clear all tasks with confirmation → verify empty state
- [ ] T038 [US1] Test: Reload tasks → verify count displayed in message

**Checkpoint**: At this point, User Story 1 should be fully functional - basic task CRUD works independently

---

## Phase 4: User Story 2 - Status Management (Priority: P2)

**Goal**: Users can define custom status labels and assign them to tasks to organize work by state

**Independent Test**: Create custom statuses, assign them to tasks, view tasks with status labels

### Implementation for User Story 2

- [ ] T039 [P] [US2] Create StatusController in backend/src/main/java/com/taskmanager/controller/StatusController.java with GET /api/statuses endpoint
- [ ] T040 [P] [US2] Add POST /api/statuses endpoint to StatusController for creating statuses
- [ ] T041 [P] [US2] Add PUT /api/statuses/{id} endpoint to StatusController for updating status labels
- [ ] T042 [P] [US2] Add DELETE /api/statuses/{id} endpoint to StatusController for deleting statuses
- [ ] T043 [P] [US2] Implement status API methods in frontend/src/services/api.js: getStatuses, createStatus, updateStatus, deleteStatus
- [ ] T044 [US2] Create StatusManager component in frontend/src/components/StatusManager.jsx for CRUD operations on statuses
- [ ] T045 [US2] Update TaskForm component to include status dropdown populated from getStatuses API
- [ ] T046 [US2] Update TaskList component to display status labels with color badges
- [ ] T047 [US2] Update App.jsx to load statuses on mount and pass to child components
- [ ] T048 [US2] Add status creation handler in StatusManager calling createStatus API
- [ ] T049 [US2] Add status update handler in StatusManager calling updateStatus API
- [ ] T050 [US2] Add status deletion handler in StatusManager calling deleteStatus API
- [ ] T051 [US2] Add validation in StatusManager: label 1-50 characters, prevent duplicates
- [ ] T052 [US2] Update Task model to ensure statusId is set during creation
- [ ] T053 [US2] Test: Create status "Blocked" with color #ff6b6b → verify in backend/data/statuses.json
- [ ] T054 [US2] Test: Create task with "Blocked" status → verify task shows correct status label
- [ ] T055 [US2] Test: Edit status label from "In Progress" to "Working" → verify all tasks reflect update
- [ ] T056 [US2] Test: Delete status → verify handling (prevent if tasks assigned, or reassign to default)

**Checkpoint**: At this point, User Stories 1 AND 2 should both work - tasks have custom statuses

---

## Phase 5: User Story 3 - Data Persistence and Reliability (Priority: P3)

**Goal**: Tasks persist across browser sessions and are protected from data corruption during concurrent operations

**Independent Test**: Create tasks, close browser, reopen, verify tasks remain. Test concurrent operations don't corrupt data.

### Implementation for User Story 3

- [ ] T057 [US3] Verify JsonFileRepository implements file locking with FileChannel.lock() for all write operations
- [ ] T058 [US3] Add error handling in JsonFileRepository for missing files - initialize empty storage
- [ ] T059 [US3] Add error handling in JsonFileRepository for corrupted JSON - log error and initialize fresh storage
- [ ] T060 [US3] Add JSON validation in JsonFileRepository on load - reject invalid data structure
- [ ] T061 [US3] Add retry logic in JsonFileRepository for file lock acquisition with timeout
- [ ] T062 [US3] Update TaskRepository to handle save failures and return error to controller
- [ ] T063 [US3] Update StatusRepository to handle save failures and return error to controller
- [ ] T064 [US3] Update TaskController to return appropriate HTTP status codes on save failures (500 Internal Server Error)
- [ ] T065 [US3] Update StatusController to return appropriate HTTP status codes on save failures
- [ ] T066 [US3] Update App.jsx error handling to display file operation errors to user
- [ ] T067 [US3] Test: Create 5 tasks, close browser, reopen → verify all 5 tasks displayed
- [ ] T068 [US3] Test: Simulate concurrent writes (open multiple browser tabs) → verify no data corruption
- [ ] T069 [US3] Test: Delete backend/data/tasks.json, reload app → verify empty state with no errors
- [ ] T070 [US3] Test: Corrupt JSON file with invalid syntax → verify error message and fresh initialization
- [ ] T071 [US3] Test: Save operation fails → verify user sees error notification

**Checkpoint**: All core user stories are now independently functional with reliable persistence

---

## Phase 6: User Story 4 - User Interface and Experience (Priority: P4)

**Goal**: Clean, responsive interface with immediate feedback for all actions and graceful error handling

**Independent Test**: Perform all operations and verify visual feedback, responsive layout, error handling

### Implementation for User Story 4

- [ ] T072 [P] [US4] Create App.css in frontend/src/styles/App.css with brown header bar (#5c4033)
- [ ] T073 [P] [US4] Add button styling to App.css: blue add (#4a90e2), red delete (#d9534f), orange clear (#f0ad4e), cyan reload (#5bc0de)
- [ ] T074 [P] [US4] Add table styling to App.css with alternating row colors and borders
- [ ] T075 [P] [US4] Add status badge styling to App.css with color indicators
- [ ] T076 [P] [US4] Add responsive layout to App.css with max-width 1200px container
- [ ] T077 [P] [US4] Add message banner styling to App.css for success (green) and error (red) states
- [ ] T078 [US4] Update MessageBanner component to auto-dismiss after 3 seconds
- [ ] T079 [US4] Add loading states to App.jsx during API calls
- [ ] T080 [US4] Add transitions and hover effects to buttons in App.css
- [ ] T081 [US4] Update TaskForm validation to show inline error messages
- [ ] T082 [US4] Add empty state message in TaskList when no tasks exist
- [ ] T083 [US4] Add confirmation dialog for destructive actions (clear all, delete status)
- [ ] T084 [US4] Test: Add task → verify success message displays for 3 seconds
- [ ] T085 [US4] Test: Submit empty task → verify validation error displays
- [ ] T086 [US4] Test: View on mobile (320px width) → verify responsive layout
- [ ] T087 [US4] Test: Backend down → verify user-friendly error message

**Checkpoint**: All user stories complete with polished UI/UX

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T088 [P] Add XSS prevention - HTML entity encoding in TaskController for task text
- [ ] T089 [P] Add input validation annotations to Task and Status models (@NotBlank, @Size)
- [ ] T090 [P] Add logging to TaskRepository and StatusRepository for all file operations
- [ ] T091 [P] Update README.md with setup instructions and running commands
- [ ] T092 [P] Create quickstart validation script to test all user stories
- [ ] T093 Code cleanup: Remove console.log statements from frontend
- [ ] T094 Code cleanup: Ensure consistent error handling patterns across controllers
- [ ] T095 Performance: Verify API response times under 200ms for all endpoints
- [ ] T096 Security: Verify CORS configuration only allows frontend origin
- [ ] T097 Documentation: Add inline comments to complex file locking logic
- [ ] T098 Final validation: Run through all user story test scenarios
- [ ] T099 Final validation: Verify JSON files are properly formatted and valid
- [ ] T100 Final validation: Test with original input/tasks.json data migrated to new format

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3-6)**: All depend on Foundational phase completion
  - User stories can then proceed in parallel (if staffed)
  - Or sequentially in priority order (P1 → P2 → P3 → P4)
- **Polish (Phase 7)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P2)**: Can start after Foundational (Phase 2) - Integrates with US1 but independently testable
- **User Story 3 (P3)**: Can start after Foundational (Phase 2) - Enhances US1 and US2 reliability
- **User Story 4 (P4)**: Can start after Foundational (Phase 2) - Enhances all stories' UX

### Within Each User Story

- Backend controllers before frontend API integration
- API service layer before React components
- Core components before integration in App.jsx
- Implementation before testing
- Story complete before moving to next priority

### Parallel Opportunities

- All Setup tasks marked [P] can run in parallel
- All Foundational tasks marked [P] can run in parallel (within Phase 2)
- Once Foundational phase completes, all user stories can start in parallel (if team capacity allows)
- Backend and frontend tasks within a story marked [P] can run in parallel
- Different user stories can be worked on in parallel by different team members

---

## Parallel Example: User Story 1

```bash
# Launch backend endpoints together:
Task: "Create TaskController with GET /api/tasks endpoint"
Task: "Add POST /api/tasks endpoint to TaskController"
Task: "Add DELETE /api/tasks/{id} endpoint to TaskController"
Task: "Add DELETE /api/tasks endpoint to TaskController"
Task: "Add POST /api/tasks/reload endpoint to TaskController"

# Launch frontend components together:
Task: "Create TaskList component in frontend/src/components/TaskList.jsx"
Task: "Create TaskForm component in frontend/src/components/TaskForm.jsx"
Task: "Create MessageBanner component in frontend/src/components/MessageBanner.jsx"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup (T001-T010)
2. Complete Phase 2: Foundational (T011-T018) - CRITICAL - blocks all stories
3. Complete Phase 3: User Story 1 (T019-T038)
4. **STOP and VALIDATE**: Test User Story 1 independently
5. Deploy/demo if ready - basic task management works!

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP - basic CRUD!)
3. Add User Story 2 → Test independently → Deploy/Demo (custom statuses!)
4. Add User Story 3 → Test independently → Deploy/Demo (reliable persistence!)
5. Add User Story 4 → Test independently → Deploy/Demo (polished UX!)
6. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1 (Basic Task Management)
   - Developer B: User Story 2 (Status Management)
   - Developer C: User Story 3 (Data Persistence)
   - Developer D: User Story 4 (UI/UX)
3. Stories complete and integrate independently

---

## Task Summary

- **Total Tasks**: 100
- **Setup**: 10 tasks
- **Foundational**: 8 tasks (BLOCKING)
- **User Story 1 (P1)**: 20 tasks - Basic Task Management
- **User Story 2 (P2)**: 18 tasks - Status Management
- **User Story 3 (P3)**: 15 tasks - Data Persistence & Reliability
- **User Story 4 (P4)**: 16 tasks - UI/UX
- **Polish**: 13 tasks

### Parallel Opportunities Identified

- **Setup Phase**: 7 parallel tasks
- **Foundational Phase**: 4 parallel tasks
- **User Story 1**: 6 parallel tasks
- **User Story 2**: 5 parallel tasks
- **User Story 4**: 7 parallel tasks
- **Polish Phase**: 9 parallel tasks

### Independent Test Criteria

- **US1**: Create, view, delete tasks → verify JSON file updates
- **US2**: Create custom statuses, assign to tasks → verify status labels display
- **US3**: Persist across sessions, concurrent operations → verify no corruption
- **US4**: All actions provide feedback, responsive layout → verify UX quality

### Suggested MVP Scope

**Minimum Viable Product**: Complete through User Story 1 (Phase 3)
- Users can create, view, and delete tasks
- Tasks persist to JSON file
- Basic UI with success/error messages
- **Estimated**: 38 tasks (Setup + Foundational + US1)

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Tests are manual validation per constitution (prototype-only approach)
- Focus on quick prototyping with local validation
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence
