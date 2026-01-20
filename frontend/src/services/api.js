import axios from 'axios';

/**
 * API service layer for communicating with the backend.
 * All API calls go through this centralized service.
 */

// Create axios instance with base configuration
const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10 second timeout
});

// Add response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error);
    
    if (error.response) {
      // Server responded with error status
      throw new Error(error.response.data.message || `Server error: ${error.response.status}`);
    } else if (error.request) {
      // Request made but no response received
      throw new Error('No response from server. Please check if the backend is running.');
    } else {
      // Error in request configuration
      throw new Error(error.message || 'Request failed');
    }
  }
);

// ===== Task API Methods =====

/**
 * Get all tasks
 * @returns {Promise<Array>} Array of tasks
 */
export const getTasks = async () => {
  const response = await api.get('/tasks');
  return response.data;
};

/**
 * Create a new task
 * @param {Object} task - Task object with text and statusId
 * @returns {Promise<Object>} Created task
 */
export const createTask = async (task) => {
  const response = await api.post('/tasks', task);
  return response.data;
};

/**
 * Delete a task by ID
 * @param {string} id - Task ID
 * @returns {Promise<void>}
 */
export const deleteTask = async (id) => {
  await api.delete(`/tasks/${id}`);
};

/**
 * Clear all tasks
 * @returns {Promise<void>}
 */
export const clearTasks = async () => {
  await api.delete('/tasks');
};

/**
 * Reload tasks from file
 * @returns {Promise<Array>} Reloaded tasks
 */
export const reloadTasks = async () => {
  const response = await api.post('/tasks/reload');
  return response.data;
};

// ===== Status API Methods =====

/**
 * Get all statuses
 * @returns {Promise<Array>} Array of statuses
 */
export const getStatuses = async () => {
  const response = await api.get('/statuses');
  return response.data;
};

/**
 * Create a new status
 * @param {Object} status - Status object with label and color
 * @returns {Promise<Object>} Created status
 */
export const createStatus = async (status) => {
  const response = await api.post('/statuses', status);
  return response.data;
};

/**
 * Update an existing status
 * @param {string} id - Status ID
 * @param {Object} status - Updated status object
 * @returns {Promise<Object>} Updated status
 */
export const updateStatus = async (id, status) => {
  const response = await api.put(`/statuses/${id}`, status);
  return response.data;
};

/**
 * Delete a status by ID
 * @param {string} id - Status ID
 * @returns {Promise<void>}
 */
export const deleteStatus = async (id) => {
  await api.delete(`/statuses/${id}`);
};

export default api;
