# Event Registration System - Frontend

A modern React application for managing event registrations with JWT authentication.

## Features

✅ **JWT Authentication** - Secure login with Bearer tokens
✅ **Event Management** - View all events with filtering
✅ **Event Registration** - Register and cancel event registrations
✅ **Availability Tracking** - Real-time capacity and seat availability
✅ **Responsive Design** - Mobile-friendly with Tailwind CSS
✅ **Protected Routes** - Automatic redirect to login if not authenticated

## Tech Stack

- **React 18** - UI library
- **Vite** - Build tool (faster than CRA)
- **React Router v6** - Navigation
- **Axios** - HTTP client with interceptors
- **Tailwind CSS** - Styling
- **Context API** - State management (Auth)

## Folder Structure

```
src/
├── api/              # Axios API client & endpoints
├── components/       # Reusable UI components
│   ├── Navbar.jsx
│   ├── EventCard.jsx
│   └── ProtectedRoute.jsx
├── pages/            # Page components
│   ├── LoginPage.jsx
│   ├── EventsPage.jsx
│   └── EventDetailPage.jsx
├── context/          # Auth context for state
├── hooks/            # Custom React hooks
├── utils/            # Helper functions
├── App.jsx           # Main app component
├── main.jsx          # Entry point
└── index.css         # Global styles
```

## Quick Start

### Prerequisites
- Node.js 16+
- Backend running on http://localhost:8080

### Installation

```bash
cd frontend
npm install
```

### Development

```bash
npm run dev
```

The app will open at `http://localhost:5173`

### Build for Production

```bash
npm run build
npm run preview
```

## API Configuration

The frontend communicates with the backend at `http://localhost:8080`.

Edit `src/api/apiClient.js` to change the base URL:

```javascript
const API_BASE_URL = 'http://localhost:8080';
```

## Authentication Flow

1. **Login Page** - User enters credentials
2. **JWT Storage** - Token saved in localStorage
3. **Axios Interceptor** - Token automatically added to requests
4. **Protected Routes** - Redirect to login if token missing
5. **Logout** - Clear token and redirect to login

## Demo Credentials

| Role  | Username | Password    |
|-------|----------|-------------|
| Admin | root     | Djaadhi_09  |
| User  | user     | 1234        |
| User  | Kabilan  | 1234        |

## Components

### LoginPage
- Username & password input
- Error handling
- Credential validation

### EventsPage
- Fetch and display all events
- Filter by upcoming/past
- User ID input for registration
- Event cards with availability

### EventDetailPage
- Full event information
- Availability metrics
- Registration form
- Date/time formatting

### EventCard
- Event summary
- Capacity & availability
- Status badge (OPEN/CLOSED/FULL)
- Register/Cancel buttons

### ProtectedRoute
- Checks JWT authentication
- Redirects to login if unauthorized

### Navbar
- User welcome message
- Logout button
- Branding

## Error Handling

- **Login Errors** - Display error message
- **Network Errors** - Show user-friendly messages
- **401 Unauthorized** - Auto-redirect to login
- **Validation** - Client-side validation on forms

## Styling

Uses Tailwind CSS utility classes for:
- Responsive grid layouts
- Card components
- Button styles
- Color schemes
- Animations (fade-in)

## Features Implemented

✅ JWT Authentication with localStorage
✅ Axios interceptors for token injection
✅ Protected routes with AuthContext
✅ Event listing with filtering
✅ Event detail page
✅ Registration management
✅ Responsive design
✅ Error handling
✅ Loading states
✅ User feedback (alerts)

## Environment Variables

Create `.env` file (optional):

```
VITE_API_BASE_URL=http://localhost:8080
```

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Future Enhancements

- [ ] User profile page
- [ ] Edit event details (ADMIN)
- [ ] Advanced filtering
- [ ] Event search
- [ ] Waitlist UI
- [ ] Export registrations
- [ ] Email notifications

## License

MIT

## Support

Backend API: http://localhost:8080
Swagger API Docs: http://localhost:8080/swagger-ui/index.html

