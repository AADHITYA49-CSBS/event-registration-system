import React from 'react';

export const EventCard = ({ event, onRegister, onCancel, isRegistered }) => {
  const availableSeats = event.capacity - (event.registrationCount || 0);
  const isFull = availableSeats <= 0;
  const isClosed = event.status === 'CLOSED';

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
    <div className="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow p-6 fade-in">
      <h3 className="text-lg font-semibold text-gray-800 mb-2">{event.title}</h3>
      
      <div className="space-y-2 mb-4 text-sm text-gray-600">
        <p>
          📅 <span className="font-medium">{formatDate(event.date)}</span>
        </p>
        <p>
          👥 Capacity: <span className="font-medium">{event.capacity}</span>
        </p>
        <p>
          🪑 Available: <span className="font-medium text-blue-600">{Math.max(0, availableSeats)}</span>
        </p>
      </div>

      <div className="flex items-center justify-between">
        <span
          className={`px-3 py-1 rounded-full text-xs font-semibold ${
            isClosed
              ? 'bg-red-100 text-red-800'
              : isFull
              ? 'bg-yellow-100 text-yellow-800'
              : 'bg-green-100 text-green-800'
          }`}
        >
          {isClosed ? 'CLOSED' : isFull ? 'FULL' : 'OPEN'}
        </span>

        <div className="flex gap-2">
          {!isRegistered ? (
            <button
              onClick={() => onRegister(event.id)}
              disabled={isFull || isClosed}
              className="px-4 py-2 bg-blue-600 hover:bg-blue-700 disabled:bg-gray-300 text-white rounded transition text-sm font-medium"
            >
              Register
            </button>
          ) : (
            <button
              onClick={() => onCancel()}
              className="px-4 py-2 bg-red-600 hover:bg-red-700 text-white rounded transition text-sm font-medium"
            >
              Cancel
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

