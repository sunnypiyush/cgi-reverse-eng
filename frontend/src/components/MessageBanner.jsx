import React, { useEffect } from 'react';
import PropTypes from 'prop-types';

/**
 * MessageBanner component - Displays success/error messages
 */
const MessageBanner = ({ message, type, onDismiss }) => {
  // Auto-dismiss after 3 seconds
  useEffect(() => {
    if (message) {
      const timer = setTimeout(() => {
        onDismiss();
      }, 3000);

      return () => clearTimeout(timer);
    }
  }, [message, onDismiss]);

  if (!message) {
    return null;
  }

  return (
    <div className={`message-banner message-${type}`}>
      <span className="message-text">{message}</span>
      <button 
        className="message-close" 
        onClick={onDismiss}
        aria-label="Dismiss message"
      >
        ×
      </button>
    </div>
  );
};

MessageBanner.propTypes = {
  message: PropTypes.string,
  type: PropTypes.oneOf(['success', 'error']).isRequired,
  onDismiss: PropTypes.func.isRequired,
};

MessageBanner.defaultProps = {
  message: '',
};

export default MessageBanner;
