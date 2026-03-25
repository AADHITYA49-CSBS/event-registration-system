import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add interceptor to include JWT token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('accessToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  login: (username, password) =>
    api.post('/auth/login', { username, password }),
};

export const eventAPI = {
  getEvents: (filter) =>
    api.get('/events', { params: filter ? { filter } : {} }),
  getEventById: (id) => api.get(`/events/${id}`),
  getAvailability: (id) => api.get(`/events/${id}/availability`),
  createEvent: (data) => api.post('/events', data),
};

export const registrationAPI = {
  register: (eventId, userId) =>
    api.post('/register', { eventId, userId }),
  cancelRegistration: (id) => api.delete(`/register/${id}`),
};

export default api;

