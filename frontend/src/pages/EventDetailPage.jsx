import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { eventAPI, registrationAPI } from '../api/apiClient';
import { Navbar } from '../components/Navbar';

export const EventDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [event, setEvent] = useState(null);
  const [availability, setAvailability] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [userId, setUserId] = useState('101');
  const [registering, setRegistering] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [eventRes, availRes] = await Promise.all([
          eventAPI.getEventById(id),
          eventAPI.getAvailability(id),
        ]);
        setEvent(eventRes.data);
        setAvailability(availRes.data);
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to load event');
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [id]);

  const handleRegister = async () => {
    if (!userId.trim()) {
      setError('Please enter a User ID');
      return;
    }
    setRegistering(true);
    try {
      await registrationAPI.register(parseInt(id), parseInt(userId));
      alert('Registration successful!');
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed');
    } finally {
      setRegistering(false);
    }
  };

  if (loading) {
    return (
      <>
        <Navbar />
        <div className="flex items-center justify-center min-h-screen">
          <div className="text-gray-600">Loading...</div>
        </div>
      </>
    );
  }

  return (
    <>
      <Navbar />
      <div className="min-h-screen bg-gray-50 py-8">
        <div className="max-w-2xl mx-auto px-4">
          <button
            onClick={() => navigate('/')}
            className="text-blue-600 hover:text-blue-800 mb-6 flex items-center gap-2"
          >
            ← Back to Events
          </button>

          {error && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded mb-6">
              {error}
            </div>
          )}

          {event && (
            <div className="bg-white rounded-lg shadow-lg p-8 fade-in">
              <h1 className="text-3xl font-bold text-gray-800 mb-4">{event.title}</h1>

              <div className="grid grid-cols-2 gap-6 mb-8">
                <div>
                  <p className="text-gray-600 text-sm">Date & Time</p>
                  <p className="text-lg font-semibold text-gray-800">
                    {new Date(event.date).toLocaleDateString('en-US', {
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric',
                      hour: '2-digit',
                      minute: '2-digit',
                    })}
                  </p>
                </div>

                <div>
                  <p className="text-gray-600 text-sm">Status</p>
                  <span
                    className={`px-3 py-1 rounded-full text-sm font-semibold ${
                      event.status === 'CLOSED'
                        ? 'bg-red-100 text-red-800'
                        : 'bg-green-100 text-green-800'
                    }`}
                  >
                    {event.status}
                  </span>
                </div>
              </div>

              {availability && (
                <div className="bg-blue-50 border border-blue-200 rounded-lg p-6 mb-8">
                  <h2 className="text-xl font-semibold text-gray-800 mb-4">
                    Availability
                  </h2>
                  <div className="grid grid-cols-3 gap-4">
                    <div>
                      <p className="text-gray-600 text-sm">Total Capacity</p>
                      <p className="text-2xl font-bold text-blue-600">
                        {availability.capacity}
                      </p>
                    </div>
                    <div>
                      <p className="text-gray-600 text-sm">Registered</p>
                      <p className="text-2xl font-bold text-orange-600">
                        {availability.registrationCount}
                      </p>
                    </div>
                    <div>
                      <p className="text-gray-600 text-sm">Available Seats</p>
                      <p
                        className={`text-2xl font-bold ${
                          availability.capacity - availability.registrationCount > 0
                            ? 'text-green-600'
                            : 'text-red-600'
                        }`}
                      >
                        {Math.max(0, availability.capacity - availability.registrationCount)}
                      </p>
                    </div>
                  </div>
                </div>
              )}

              {event.status !== 'CLOSED' &&
                availability &&
                availability.capacity > availability.registrationCount && (
                  <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-6">
                    <h2 className="text-xl font-semibold text-gray-800 mb-4">
                      Register for this Event
                    </h2>
                    <div className="space-y-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                          User ID
                        </label>
                        <input
                          type="number"
                          value={userId}
                          onChange={(e) => setUserId(e.target.value)}
                          placeholder="Enter your User ID"
                          className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                        />
                      </div>
                      <button
                        onClick={handleRegister}
                        disabled={registering}
                        className="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 text-white font-bold py-2 px-4 rounded-lg transition"
                      >
                        {registering ? 'Registering...' : 'Register Now'}
                      </button>
                    </div>
                  </div>
                )}
            </div>
          )}
        </div>
      </div>
    </>
  );
};

