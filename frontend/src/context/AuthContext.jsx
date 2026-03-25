import React, { createContext, useState, useContext, useEffect } from 'react';
import api from '../api/axios';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');
    if (token && username) {
      setUser({ username, token });
    }
    setLoading(false);
  }, []);

  const login = async (username, password = "password") => {
    try {
      // Typically we call the auth token endpoint if the backend supports it, e.g. /auth/generateToken
      // Since it's a simple dashboard, we assume an API exists for auth. Adjust this if backend is different.
      const response = await api.post('/auth/generateToken', { username, password });
      const token = response.data;
      
      localStorage.setItem('token', token);
      localStorage.setItem('username', username);
      setUser({ username, token });
      return true;
    } catch (error) {
      console.error('Login failed', error);
      return false;
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
