import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { eventService } from '../api/eventService';
import { useAuth } from '../context/AuthContext';

const EventDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  
  const [event, setEvent] = useState(null);
  const [availability, setAvailability] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchDetails = async () => {
      try {
        const [eventRes, availabilityRes] = await Promise.all([
          eventService.getEventById(id).catch(e => { throw e; }),
          // availability might fail if backend endpoint doesn't exactly match yet, but we'll try
          eventService.getAvailability(id).catch(() => ({ data: { remainingSlots: '?', isWaitlistActive: false } }))
        ]);
        
        setEvent(eventRes.data);
        setAvailability(availabilityRes.data);
      } catch (err) {
        setError('Failed to load event details.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchDetails();
  }, [id]);

  const handleRegister = async () => {
    try {
      await eventService.register(id, user.username);
      // Refresh
      window.location.reload();
    } catch (err) {
      alert('Registration failed. ' + (err.response?.data || ''));
    }
  };

  if (loading) return <div className="min-h-screen flex items-center justify-center">Loading details...</div>;
  if (error || !event) return <div className="min-h-screen flex items-center justify-center text-red-600">{error || 'Event not found'}</div>;

  return (
    <div className="min-h-screen bg-slate-50 py-8">
      <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
        <button 
          onClick={() => navigate('/events')}
          className="mb-6 flex items-center text-indigo-600 hover:text-indigo-800 text-sm font-medium transition-colors"
        >
          ← Back to Events
        </button>

        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
          <div className="p-8">
            <div className="flex justify-between items-start mb-6">
              <h1 className="text-3xl font-bold text-gray-900">{event.name || event.title || `Event #${event.id}`}</h1>
              <span className={`inline-flex items-center px-3 py-1 rounded-full text-sm font-semibold ${
                event.status === 'OPEN' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
              }`}>
                {event.status || 'OPEN'}
              </span>
            </div>

            <div className="space-y-4 mb-8 text-gray-600">
              <div className="flex items-center">
                <span className="w-8 text-xl">📅</span>
                <span>{event.date ? new Date(event.date).toLocaleString() : 'Date TBA'}</span>
              </div>
              <div className="flex items-center">
                <span className="w-8 text-xl">📍</span>
                <span>{event.location || 'Location TBA'}</span>
              </div>
              <p className="mt-4 leading-relaxed">{event.description || 'No description provided.'}</p>
            </div>

            <div className="bg-blue-50 rounded-xl p-6 mb-8 border border-blue-100">
              <h3 className="font-semibold text-blue-900 mb-2">Availability Status</h3>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="text-sm text-blue-700">Total Capacity</p>
                  <p className="text-2xl font-bold text-blue-900">{event.capacity || 'N/A'}</p>
                </div>
                <div>
                  <p className="text-sm text-blue-700">Remaining Slots</p>
                  <p className="text-2xl font-bold text-blue-900">{availability?.remainingSlots ?? 'N/A'}</p>
                </div>
              </div>
              {availability?.isWaitlistActive && (
                <div className="mt-4 inline-flex px-3 py-1 bg-yellow-100 text-yellow-800 rounded-lg text-sm font-medium">
                  ⚠️ Waitlist is currently active
                </div>
              )}
            </div>

            <div className="flex gap-4">
              <button
                onClick={handleRegister}
                disabled={event.status === 'CLOSED'}
                className={`w-full py-3 px-6 rounded-xl font-bold transition-all shadow-md ${
                  event.status === 'CLOSED'
                    ? 'bg-gray-200 text-gray-400 cursor-not-allowed shadow-none'
                    : 'bg-indigo-600 text-white hover:bg-indigo-700 hover:shadow-lg'
                }`}
              >
                {event.status === 'CLOSED' ? 'Event Closed' : 'Register Now'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EventDetailPage;
