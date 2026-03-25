# 🚀 Event Registration System - Complete Setup Guide

## Project Structure

```
event-registration-system/
├── src/                          (Java Backend - Spring Boot)
│   ├── main/java/com/event/...
│   └── test/java/
├── frontend/                      (React Frontend - Vite)
│   ├── src/
│   │   ├── api/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── context/
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── package.json
│   ├── vite.config.js
│   └── README.md
├── pom.xml                        (Maven config)
└── README.md
```

---

## 🔧 Backend Setup (Spring Boot)

### Prerequisites
- Java 17+
- Maven
- MySQL 8.0+

### Installation

1. **Clone or navigate to project**
   ```bash
   cd event-registration-system-1
   ```

2. **Database Setup**
   ```sql
   CREATE DATABASE event_registration_system;
   ```

3. **Configure Database** (src/main/resources/application.properties)
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/event_registration_system
   spring.datasource.username=root
   spring.datasource.password=Djaadhi_09
   ```

4. **Build Backend**
   ```bash
   mvn clean install
   ```

5. **Run Backend**
   ```bash
   mvn spring-boot:run
   ```

**Backend Running At:** `http://localhost:8080`
**Swagger UI:** `http://localhost:8080/swagger-ui/index.html`

---

## 🎨 Frontend Setup (React)

### Prerequisites
- Node.js 16+ & npm

### Installation

1. **Navigate to Frontend**
   ```bash
   cd frontend
   ```

2. **Install Dependencies**
   ```bash
   npm install
   ```

3. **Run Development Server**
   ```bash
   npm run dev
   ```

**Frontend Running At:** `http://localhost:5173`

4. **Build for Production**
   ```bash
   npm run build
   npm run preview
   ```

---

## 🔐 Login Credentials

Use these to test the application:

### Admin User (Can Create Events)
```
Username: root
Password: Djaadhi_09
```

### Regular Users (Can Register/Cancel)
```
Username: user
Password: 1234

Username: Kabilan
Password: 1234
```

---

## ✨ Frontend Features

### Pages

| Page | Route | Features |
|------|-------|----------|
| Login | `/login` | JWT authentication, credentials input |
| Events | `/` | List all events, filter (upcoming/past), register |
| Event Detail | `/events/:id` | Full event info, availability, register form |

### Components

| Component | Purpose |
|-----------|---------|
| LoginPage | User authentication |
| EventsPage | List and manage events |
| EventDetailPage | Event details and registration |
| EventCard | Event summary card |
| Navbar | Navigation and logout |
| ProtectedRoute | Auth guard for routes |

### Features Implemented

✅ JWT Authentication with localStorage
✅ Axios interceptors for Bearer tokens
✅ Protected routes (redirect to login if no token)
✅ Event listing with filtering
✅ Event detail page
✅ Registration management
✅ Responsive Tailwind CSS design
✅ Error handling
✅ Loading states
✅ User feedback (alerts)

---

## 🔌 API Integration

### Base URL
```
http://localhost:8080
```

### Key Endpoints

**Authentication**
```
POST /auth/login
Body: { "username": "root", "password": "Djaadhi_09" }
```

**Events**
```
GET  /events                    (No auth required)
GET  /events?filter=upcoming    (No auth required)
GET  /events/{id}               (No auth required)
GET  /events/{id}/availability  (No auth required)
POST /events                    (ADMIN only)
```

**Registration**
```
POST   /register               (USER/ADMIN)
DELETE /register/{id}          (USER/ADMIN)
```

---

## 📋 Full Workflow

### Step 1: Start Backend
```bash
cd event-registration-system-1
mvn spring-boot:run
```
✅ Wait for: "Tomcat started on port(s): 8080"

### Step 2: Start Frontend
```bash
cd frontend
npm run dev
```
✅ App opens at http://localhost:5173

### Step 3: Login
- Username: `root`
- Password: `Djaadhi_09`

### Step 4: Create Event (Admin)
1. Click "Create Event" (if visible)
2. Or use Swagger UI: http://localhost:8080/swagger-ui/index.html

### Step 5: View Events
- See all events on dashboard
- Filter by upcoming/past
- Click card to view details

### Step 6: Register for Event
1. Enter User ID (e.g., 101)
2. Click "Register"
3. See registration status

### Step 7: Cancel Registration
1. Click "Cancel" on event
2. Confirm cancellation

---

## 🛠️ Development

### Frontend Development Commands

```bash
# Install dependencies
npm install

# Start dev server (hot reload)
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Install a package
npm install <package-name>
```

### Backend Development Commands

```bash
# Build
mvn clean install

# Run with hot reload (devtools)
mvn spring-boot:run

# Run tests
mvn test

# Build JAR
mvn clean package
```

---

## 📁 File Structure - Frontend

```
frontend/
├── src/
│   ├── api/
│   │   └── apiClient.js          # Axios setup & endpoints
│   ├── components/
│   │   ├── EventCard.jsx         # Event display card
│   │   ├── Navbar.jsx            # Top navigation
│   │   └── ProtectedRoute.jsx    # Auth guard
│   ├── pages/
│   │   ├── LoginPage.jsx         # Login form
│   │   ├── EventsPage.jsx        # Events list
│   │   └── EventDetailPage.jsx   # Event details
│   ├── context/
│   │   └── AuthContext.jsx       # Auth state management
│   ├── App.jsx                   # Main app component
│   ├── main.jsx                  # Entry point
│   └── index.css                 # Global styles
├── index.html                    # HTML entry
├── package.json                  # Dependencies
├── vite.config.js                # Vite config
├── tailwind.config.js            # Tailwind config
├── postcss.config.js             # PostCSS config
└── README.md                     # Frontend docs
```

---

## 🐛 Troubleshooting

### Frontend Won't Connect to Backend

**Error:** `NetworkError: Failed to fetch`

**Solution:**
```bash
# Check backend is running
curl http://localhost:8080/swagger-ui/index.html

# Verify API_BASE_URL in src/api/apiClient.js
# Should be: http://localhost:8080
```

### Login Failed

**Error:** `Invalid username or password`

**Solution:**
- Check credentials (case-sensitive)
- Ensure backend is running
- Check network tab in browser DevTools

### Port Already in Use

**Frontend (5173):**
```bash
# Kill process on port 5173
netstat -ano | findstr :5173
taskkill /PID <PID> /F
```

**Backend (8080):**
```bash
# Kill process on port 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

---

## 📚 Technology Stack

### Backend
- Java 17
- Spring Boot 3.5.13
- Spring Security + JWT
- JPA/Hibernate
- MySQL 8.0
- Maven
- Swagger OpenAPI 3.0

### Frontend
- React 18.2
- Vite 5.0
- React Router v6
- Axios 1.6
- Tailwind CSS 3.3
- Context API

---

## 🔗 GitHub Branches

### Backend Features
- `feature/jwt-auth-stateles` - JWT authentication
- `feature/validation-rbac-transactions` - Validation & Authorization

### Frontend
- `feature/frontend-react` - Complete React application

### Create PRs
```
https://github.com/AADHITYA49-CSBS/event-registration-system/pull/new/<branch>
```

---

## 🚀 Deployment

### Frontend (Vercel)
```bash
npm run build
# Deploy dist/ folder
```

### Backend (Docker)
```bash
mvn clean package
docker build -t event-registration .
docker run -p 8080:8080 event-registration
```

---

## ✅ Testing Checklist

- [ ] Backend starts without errors
- [ ] Frontend starts and loads
- [ ] Can login with credentials
- [ ] Can see events on dashboard
- [ ] Can register for event
- [ ] Can cancel registration
- [ ] Can filter events
- [ ] Logout works
- [ ] Protected routes redirect to login
- [ ] Error messages display correctly

---

## 📞 Support

**Backend API Docs:** http://localhost:8080/swagger-ui/index.html
**Frontend Dev Server:** http://localhost:5173
**Git Repository:** https://github.com/AADHITYA49-CSBS/event-registration-system

---

## 📝 Notes

- Backend uses JWT tokens valid for 24 hours
- Frontend stores token in localStorage
- All API requests include Bearer token automatically
- Protected routes redirect to login if no token
- Demo credentials work for testing
- Both apps can run simultaneously
- Frontend fetches from backend on every action

---

**Happy coding! 🎉**

