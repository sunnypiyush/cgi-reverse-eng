import React from 'react';
import PropTypes from 'prop-types';

/**
 * TaskList component - Displays tasks in a table format
 */
const TaskList = ({ tasks, statuses, onDelete }) => {
  // Helper function to get status label by ID
  const getStatusLabel = (statusId) => {
    const status = statuses.find(s => s.id === statusId);
    return status ? status.label : 'Unknown';
  };

  // Helper function to get status color by ID
  const getStatusColor = (statusId) => {
    const status = statuses.find(s => s.id === statusId);
    return status ? status.color : '#999999';
  };

  // Format date for display
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleString();
  };

  if (tasks.length === 0) {
    return (
      <div className="empty-state">
        <p>No tasks yet. Create your first task above!</p>
      </div>
    );
  }

  return (
    <div className="task-list">
      <table>
        <thead>
          <tr>
            <th>Task</th>
            <th>Status</th>
            <th>Created</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {tasks.map((task) => (
            <tr key={task.id}>
              <td className="task-text">{task.text}</td>
              <td>
                <span 
                  className="status-badge" 
                  style={{ backgroundColor: getStatusColor(task.statusId) }}
                >
                  {getStatusLabel(task.statusId)}
                </span>
              </td>
              <td className="task-date">{formatDate(task.created)}</td>
              <td>
                <button 
                  className="btn btn-delete" 
                  onClick={() => onDelete(task.id)}
                  title="Delete task"
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

TaskList.propTypes = {
  tasks: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.string.isRequired,
    text: PropTypes.string.isRequired,
    created: PropTypes.string.isRequired,
    statusId: PropTypes.string.isRequired,
  })).isRequired,
  statuses: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.string.isRequired,
    label: PropTypes.string.isRequired,
    color: PropTypes.string.isRequired,
  })).isRequired,
  onDelete: PropTypes.func.isRequired,
};

export default TaskList;
