# Task Management System

A modern task management application built with Java Spring Boot (backend) and React (frontend), modernized from a legacy Perl CGI application.

## 🎯 Features

- ✅ Create, view, and delete tasks
- ✅ Assign custom statuses to tasks
- ✅ Persistent JSON file storage
- ✅ File locking for concurrent access safety
- ✅ Responsive web interface
- ✅ Real-time success/error notifications

## 🏗️ Architecture

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

## 📋 Prerequisites

- **Java**: JDK 17 or higher
- **Maven**: 3.6 or higher
- **Node.js**: 18 or higher
- **npm**: 9 or higher

## 🚀 Quick Start

### 1. Start the Backend

```bash
cd backend
mvn spring-boot:run
```

The backend API will start on `http://localhost:8080`

### 2. Start the Frontend

In a new terminal:

```bash
cd frontend
npm install  # First time only
npm run dev
```

The frontend will start on `http://localhost:5173`

### 3. Access the Application

Open your browser to: `http://localhost:5173`

## 📁 Project Structure

```
cgi-reverse-eng/
├── backend/                    # Java Spring Boot API
│   ├── src/main/java/com/taskmanager/
│   │   ├── model/             # Data models (Task, Status)
│   │   ├── repository/        # Data access layer
│   │   ├── controller/        # REST API endpoints
│   │   ├── config/            # Configuration (CORS, etc.)
│   │   └── TaskManagerApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── pom.xml
│   └── data/                  # JSON storage
│       ├── tasks.json
│       └── statuses.json
│
├── frontend/                  # React SPA
│   ├── src/
│   │   ├── components/       # React components
│   │   ├── services/         # API service layer
│   │   ├── styles/           # CSS styles
│   │   ├── App.jsx           # Main app component
│   │   └── main.jsx          # Entry point
│   ├── package.json
│   └── vite.config.js
│
└── specs/                    # Design documents
    └── 001-task-management-system/
        ├── spec.md           # Requirements
        ├── plan.md           # Technical plan
        └── tasks.md          # Implementation tasks
```

## 🔧 Development

### Backend Development

**Run with hot reload:**
```bash
cd backend
mvn spring-boot:run
```

**Build JAR:**
```bash
mvn clean package
```

**Run tests:**
```bash
mvn test
```

### Frontend Development

**Run dev server:**
```bash
cd frontend
npm run dev
```

**Build for production:**
```bash
npm run build
```

**Preview production build:**
```bash
npm run preview
```

## 🌐 API Endpoints

### Tasks

- `GET /api/tasks` - Get all tasks
- `POST /api/tasks` - Create a new task
- `DELETE /api/tasks/{id}` - Delete a task
- `DELETE /api/tasks` - Clear all tasks
- `POST /api/tasks/reload` - Reload tasks from file

### Statuses

- `GET /api/statuses` - Get all statuses
- `POST /api/statuses` - Create a new status
- `PUT /api/statuses/{id}` - Update a status
- `DELETE /api/statuses/{id}` - Delete a status

## 📊 Data Models

### Task
```json
{
  "id": "1737345600000-a1b2c3d4",
  "text": "Review documentation",
  "created": "2026-01-20T08:00:00Z",
  "statusId": "status-001"
}
```

### Status
```json
{
  "id": "status-001",
  "label": "To Do",
  "color": "#4a90e2"
}
```

## 🎨 Technology Stack

### Backend
- Java 17
- Spring Boot 3.2.1
- Jackson (JSON processing)
- Maven

### Frontend
- React 18
- Vite
- Axios (HTTP client)
- CSS3

## 🔒 Security Features

- XSS prevention with HTML entity encoding
- Input validation (client and server-side)
- CORS configuration for frontend access
- File locking for concurrent access safety

## 📝 Configuration

### Backend Configuration

Edit `backend/src/main/resources/application.properties`:

```properties
# Server port
server.port=8080

# File storage paths
app.storage.tasks-file=backend/data/tasks.json
app.storage.statuses-file=backend/data/statuses.json

# CORS allowed origins
app.cors.allowed-origins=http://localhost:5173,http://localhost:3000
```

### Frontend Configuration

Edit `frontend/vite.config.js`:

```javascript
export default defineConfig({
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      }
    }
  }
})
```

## 🧪 Testing

### Manual Testing Checklist

1. ✅ Create a task with different statuses
2. ✅ View tasks in the list
3. ✅ Delete individual tasks
4. ✅ Clear all tasks (with confirmation)
5. ✅ Reload tasks from file
6. ✅ Verify JSON files update correctly
7. ✅ Test input validation (empty task, long text)
8. ✅ Test error handling (backend down)
9. ✅ Test responsive layout (mobile view)

## 🐛 Troubleshooting

### Backend won't start
- Check if port 8080 is already in use
- Verify Java 17+ is installed: `java -version`
- Check Maven is installed: `mvn -version`

### Frontend won't start
- Check if port 5173 is already in use
- Verify Node.js is installed: `node -version`
- Run `npm install` to install dependencies

### API calls failing
- Verify backend is running on port 8080
- Check browser console for CORS errors
- Verify proxy configuration in `vite.config.js`

### Data not persisting
- Check `backend/data/` directory exists
- Verify file permissions for JSON files
- Check backend logs for file I/O errors

## 📚 Documentation

- [Specification](specs/001-task-management-system/spec.md) - Requirements and user stories
- [Technical Plan](specs/001-task-management-system/plan.md) - Architecture and design
- [Implementation Tasks](specs/001-task-management-system/tasks.md) - Task breakdown
- [Implementation Status](IMPLEMENTATION_STATUS.md) - Current progress

## 🎯 Roadmap

### ✅ Phase 1: MVP (Complete)
- Basic task CRUD operations
- Status management
- File-based persistence
- Responsive UI

### 🚧 Phase 2: Enhancements (Planned)
- Task editing
- Search and filtering
- Pagination
- Due dates
- Task categories/tags

### 🔮 Phase 3: Advanced Features (Future)
- User authentication
- Database backend option
- Export functionality (CSV/PDF)
- Task priorities
- Task assignments

## 📄 License

This project is part of a modernization effort from legacy Perl CGI to modern Java/React stack.

## 👥 Contributing

This is a prototype project. For production use, consider:
- Adding comprehensive test suite
- Implementing user authentication
- Using a database instead of JSON files
- Adding CI/CD pipeline
- Implementing proper logging and monitoring

## 📞 Support

For issues or questions, refer to the documentation in the `specs/` directory.

---

**Status**: MVP Complete - Ready for Testing  
**Last Updated**: 2026-01-20  
**Version**: 1.0.0-SNAPSHOT
