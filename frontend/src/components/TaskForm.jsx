import React, { useState } from 'react';
import PropTypes from 'prop-types';

/**
 * TaskForm component - Form to create new tasks
 */
const TaskForm = ({ statuses, onSubmit }) => {
  const [taskText, setTaskText] = useState('');
  const [selectedStatus, setSelectedStatus] = useState('');
  const [error, setError] = useState('');

  // Set default status when statuses are loaded
  React.useEffect(() => {
    if (statuses.length > 0 && !selectedStatus) {
      setSelectedStatus(statuses[0].id);
    }
  }, [statuses, selectedStatus]);

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');

    // Validation
    if (!taskText.trim()) {
      setError('Task text is required');
      return;
    }

    if (taskText.length > 500) {
      setError('Task text must be 500 characters or less');
      return;
    }

    if (!selectedStatus) {
      setError('Please select a status');
      return;
    }

    // Submit task
    onSubmit({
      text: taskText.trim(),
      statusId: selectedStatus,
    });

    // Clear form
    setTaskText('');
    setError('');
  };

  return (
    <div className="task-form">
      <form onSubmit={handleSubmit}>
        <div className="form-row">
          <div className="form-group">
            <input
              type="text"
              className="form-input"
              placeholder="Enter task description..."
              value={taskText}
              onChange={(e) => setTaskText(e.target.value)}
              maxLength={500}
            />
            {error && <span className="error-message">{error}</span>}
          </div>
          
          <div className="form-group">
            <select
              className="form-select"
              value={selectedStatus}
              onChange={(e) => setSelectedStatus(e.target.value)}
            >
              {statuses.map((status) => (
                <option key={status.id} value={status.id}>
                  {status.label}
                </option>
              ))}
            </select>
          </div>
          
          <button type="submit" className="btn btn-add">
            Add Task
          </button>
        </div>
      </form>
    </div>
  );
};

TaskForm.propTypes = {
  statuses: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.string.isRequired,
    label: PropTypes.string.isRequired,
    color: PropTypes.string.isRequired,
  })).isRequired,
  onSubmit: PropTypes.func.isRequired,
};

export default TaskForm;
