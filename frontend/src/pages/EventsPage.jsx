import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { eventAPI, registrationAPI } from '../api/apiClient';
import { EventCard } from '../components/EventCard';
import { Navbar } from '../components/Navbar';

export const EventsPage = () => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filter, setFilter] = useState('');
  const [registrations, setRegistrations] = useState(new Set());
  const [userId, setUserId] = useState('101');
  const navigate = useNavigate();

  useEffect(() => {
    fetchEvents();
  }, [filter]);

  const fetchEvents = async () => {
    setLoading(true);
    try {
      const response = await eventAPI.getEvents(filter || undefined);
      setEvents(response.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load events');
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (eventId) => {
    if (!userId.trim()) {
      alert('Please enter a User ID');
      return;
    }
    try {
      await registrationAPI.register(eventId, parseInt(userId));
      setRegistrations((prev) => new Set([...prev, eventId]));
      alert('Registration successful!');
      fetchEvents();
    } catch (err) {
      alert(err.response?.data?.message || 'Registration failed');
    }
  };

  const handleCancel = async (registrationId) => {
    try {
      await registrationAPI.cancelRegistration(registrationId);
      setRegistrations((prev) => {
        const newSet = new Set(prev);
        newSet.delete(registrationId);
        return newSet;
      });
      alert('Cancellation successful!');
      fetchEvents();
    } catch (err) {
      alert(err.response?.data?.message || 'Cancellation failed');
    }
  };

  return (
    <>
      <Navbar />
      <div className="min-h-screen bg-gray-50 py-8">
        <div className="max-w-6xl mx-auto px-4">
          <div className="mb-8">
            <h1 className="text-4xl font-bold text-gray-800 mb-4">📅 All Events</h1>

            <div className="flex gap-4 mb-6">
              <input
                type="number"
                value={userId}
                onChange={(e) => setUserId(e.target.value)}
                placeholder="Your User ID (e.g., 101)"
                className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
              />

              <div className="flex gap-2">
                <button
                  onClick={() => { setFilter(''); fetchEvents(); }}
                  className={`px-4 py-2 rounded-lg transition ${
                    filter === ''
                      ? 'bg-blue-600 text-white'
                      : 'bg-white border border-gray-300 hover:bg-gray-50'
                  }`}
                >
                  All
                </button>
                <button
                  onClick={() => setFilter('upcoming')}
                  className={`px-4 py-2 rounded-lg transition ${
                    filter === 'upcoming'
                      ? 'bg-blue-600 text-white'
                      : 'bg-white border border-gray-300 hover:bg-gray-50'
                  }`}
                >
                  Upcoming
                </button>
                <button
                  onClick={() => setFilter('past')}
                  className={`px-4 py-2 rounded-lg transition ${
                    filter === 'past'
                      ? 'bg-blue-600 text-white'
                      : 'bg-white border border-gray-300 hover:bg-gray-50'
                  }`}
                >
                  Past
                </button>
              </div>
            </div>
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded mb-6">
              {error}
            </div>
          )}

          {loading ? (
            <div className="flex items-center justify-center py-12">
              <div className="text-gray-600">Loading events...</div>
            </div>
          ) : events.length === 0 ? (
            <div className="bg-white rounded-lg shadow p-8 text-center">
              <p className="text-gray-600 text-lg">No events found.</p>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {events.map((event) => (
                <div
                  key={event.id}
                  onClick={() => navigate(`/events/${event.id}`)}
                  className="cursor-pointer"
                >
                  <EventCard
                    event={event}
                    onRegister={() => handleRegister(event.id)}
                    onCancel={() => handleCancel(event.id)}
                    isRegistered={registrations.has(event.id)}
                  />
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </>
  );
};

