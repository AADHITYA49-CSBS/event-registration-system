import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { eventService } from '../api/eventService';
import { useAuth } from '../context/AuthContext';

const EventsPage = () => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    fetchEvents();
  }, []);

  const fetchEvents = async () => {
    try {
      const response = await eventService.getEvents();
      // Assume backend returns either an array directly or inside a data/content property
      const data = response.data?.content || response.data || [];
      setEvents(data);
    } catch (err) {
      console.error(err);
      setError('Failed to load events.');
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const handleRegister = async (eventId) => {
    try {
      await eventService.register(eventId, user.username);
      fetchEvents(); // Refresh 
    } catch (err) {
      console.error(err);
      alert('Registration failed. ' + (err.response?.data || ''));
    }
  };

  return (
    <div className="min-h-screen bg-slate-50">
      <nav className="bg-white shadow-sm sticky top-0 z-10">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16 items-center">
            <h1 className="text-xl font-bold text-indigo-600">Event Platform</h1>
            <div className="flex items-center gap-4">
              <span className="text-sm text-gray-600">👤 {user?.username}</span>
              <button 
                onClick={handleLogout}
                className="text-sm font-medium text-red-600 hover:text-red-800 transition-colors"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8 flex justify-between items-end">
          <div>
            <h2 className="text-3xl font-bold text-gray-900">Upcoming Events</h2>
            <p className="mt-2 text-gray-600">Discover and register for exciting events around you.</p>
          </div>
        </div>

        {loading ? (
          <div className="flex justify-center py-12">
            <div className="animate-spin text-indigo-600 text-3xl">🌀</div>
          </div>
        ) : error ? (
          <div className="bg-red-50 text-red-600 p-4 rounded-lg">{error}</div>
        ) : events.length === 0 ? (
          <div className="text-center py-12 bg-white rounded-2xl border border-gray-100 shadow-sm">
            <h3 className="text-lg font-medium text-gray-900">No events found</h3>
            <p className="mt-1 text-gray-500">Check back later for new updates.</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {events.map((evt) => (
              <div key={evt.id} className="bg-white rounded-2xl border border-gray-100 shadow-sm hover:shadow-md transition-all flex flex-col overflow-hidden group">
                <div className="p-6 flex-grow">
                  <div className="flex justify-between items-start mb-4">
                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                      evt.status === 'OPEN' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                    }`}>
                      {evt.status || 'OPEN'}
                    </span>
                    <span className="text-sm text-gray-500 font-medium">
                      {(evt.capacity || 0)} Total Spots
                    </span>
                  </div>
                  
                  <h3 className="text-xl font-bold text-gray-900 mb-2 group-hover:text-indigo-600 transition-colors">
                    {evt.name || evt.title || `Event #${evt.id}`}
                  </h3>
                  
                  <div className="flex items-center text-sm text-gray-500 mb-4 whitespace-nowrap">
                    <span className="mr-2">📅</span>
                    {evt.date ? new Date(evt.date).toLocaleDateString() : 'TBA'}
                  </div>
                  
                  {evt.description && (
                    <p className="text-gray-600 text-sm line-clamp-2 mb-4">
                      {evt.description}
                    </p>
                  )}
                </div>

                <div className="px-6 py-4 bg-gray-50 border-t border-gray-100 flex gap-3">
                  <button
                    onClick={() => navigate(`/events/${evt.id}`)}
                    className="flex-1 px-4 py-2 bg-white border border-gray-300 rounded-lg text-sm font-medium text-gray-700 hover:bg-gray-50 transition-colors shadow-sm"
                  >
                    Details
                  </button>
                  <button
                    onClick={() => handleRegister(evt.id)}
                    disabled={evt.status === 'CLOSED'}
                    className={`flex-1 px-4 py-2 rounded-lg text-sm font-medium shadow-sm transition-colors ${
                      evt.status === 'CLOSED' 
                        ? 'bg-gray-200 text-gray-400 cursor-not-allowed'
                        : 'bg-indigo-600 text-white hover:bg-indigo-700'
                    }`}
                  >
                    Register
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </main>
    </div>
  );
};

export default EventsPage;
