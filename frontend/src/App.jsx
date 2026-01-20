import React, { useState, useEffect } from 'react';
import TaskList from './components/TaskList';
import TaskForm from './components/TaskForm';
import MessageBanner from './components/MessageBanner';
import * as api from './services/api';
import './styles/App.css';

/**
 * Main App component - Task Management System
 */
function App() {
  const [tasks, setTasks] = useState([]);
  const [statuses, setStatuses] = useState([]);
  const [message, setMessage] = useState({ text: '', type: 'success' });
  const [loading, setLoading] = useState(false);

  // Load tasks and statuses on mount
  useEffect(() => {
    loadData();
  }, []);

  /**
   * Load tasks and statuses from API
   */
  const loadData = async () => {
    setLoading(true);
    try {
      const [tasksData, statusesData] = await Promise.all([
        api.getTasks(),
        api.getStatuses()
      ]);
      setTasks(tasksData);
      setStatuses(statusesData);
    } catch (error) {
      showError(error.message || 'Failed to load data');
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handle task creation
   */
  const handleCreateTask = async (taskData) => {
    try {
      const newTask = await api.createTask(taskData);
      setTasks([...tasks, newTask]);
      showSuccess('Task created successfully!');
    } catch (error) {
      showError(error.message || 'Failed to create task');
    }
  };

  /**
   * Handle task deletion
   */
  const handleDeleteTask = async (taskId) => {
    try {
      await api.deleteTask(taskId);
      setTasks(tasks.filter(task => task.id !== taskId));
      showSuccess('Task deleted successfully!');
    } catch (error) {
      showError(error.message || 'Failed to delete task');
    }
  };

  /**
   * Handle clear all tasks
   */
  const handleClearAll = async () => {
    if (!window.confirm('Are you sure you want to delete all tasks? This action cannot be undone.')) {
      return;
    }

    try {
      await api.clearTasks();
      setTasks([]);
      showSuccess('All tasks cleared successfully!');
    } catch (error) {
      showError(error.message || 'Failed to clear tasks');
    }
  };

  /**
   * Handle reload tasks from file
   */
  const handleReload = async () => {
    try {
      const response = await api.reloadTasks();
      setTasks(response.tasks);
      showSuccess(response.message || `Reloaded ${response.count} tasks`);
    } catch (error) {
      showError(error.message || 'Failed to reload tasks');
    }
  };

  /**
   * Show success message
   */
  const showSuccess = (text) => {
    setMessage({ text, type: 'success' });
  };

  /**
   * Show error message
   */
  const showError = (text) => {
    setMessage({ text, type: 'error' });
  };

  /**
   * Dismiss message
   */
  const dismissMessage = () => {
    setMessage({ text: '', type: 'success' });
  };

  return (
    <div className="app">
      {/* Header */}
      <header className="app-header">
        <h1>Task Management System</h1>
      </header>

      {/* Message Banner */}
      <MessageBanner
        message={message.text}
        type={message.type}
        onDismiss={dismissMessage}
      />

      {/* Main Content */}
      <main className="app-main">
        {/* Task Form */}
        <section className="task-form-section">
          <h2>Create New Task</h2>
          <TaskForm statuses={statuses} onSubmit={handleCreateTask} />
        </section>

        {/* Action Buttons */}
        <section className="action-buttons">
          <button 
            className="btn btn-clear" 
            onClick={handleClearAll}
            disabled={loading || tasks.length === 0}
          >
            Clear All Tasks
          </button>
          <button 
            className="btn btn-reload" 
            onClick={handleReload}
            disabled={loading}
          >
            Reload from File
          </button>
        </section>

        {/* Task List */}
        <section className="task-list-section">
          <h2>Tasks ({tasks.length})</h2>
          {loading ? (
            <div className="loading">Loading...</div>
          ) : (
            <TaskList 
              tasks={tasks} 
              statuses={statuses} 
              onDelete={handleDeleteTask} 
            />
          )}
        </section>
      </main>

      {/* Footer */}
      <footer className="app-footer">
        <p>Task Manager - Modernized from Perl CGI to Java Spring Boot + React</p>
      </footer>
    </div>
  );
}

export default App;
