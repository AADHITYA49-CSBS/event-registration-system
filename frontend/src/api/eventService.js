import api from './axios';

// Note: Ensure the API path matches the backend routes exactly.
export const eventService = {
  getEvents: () => api.get('/events'),
  getEventById: (id) => api.get(`/events/${id}`),
  getAvailability: (id) => api.get(`/events/${id}/availability`),
  register: (eventId, username) => api.post(`/event-registrations/register`, { eventId, username }),
  cancelRegistration: (id) => api.delete(`/event-registrations/${id}`),
};
