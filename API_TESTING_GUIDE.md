# API Testing Guide - Event Registration System

## 🔐 Authentication & Authorization

### Step 1: Login to Get JWT Token

**Endpoint:** `POST /auth/login`

**Request Body:**
```json
{
  "username": "root",
  "password": "Djaadhi_09"
}
```

**Alternative Users:**
- Username: `user`, Password: `1234` (USER role)
- Username: `Kabilan`, Password: `1234` (USER role)

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJyb290Iiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MTEzODk3MzAsImV4cCI6MTcxMTQ3NjEzMH0...",
  "tokenType": "Bearer"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "root",
    "password": "Djaadhi_09"
  }'
```

---

## 📅 Event Management (Admin Only)

### Create Event (ADMIN ONLY)

**Endpoint:** `POST /events`
**Authorization:** `ROLE_ADMIN`
**Validation:**
- Title: @NotBlank
- Date: @Future (must be in future)
- Capacity: @Positive (> 0)

**Request Headers:**
```
Authorization: Bearer <accessToken>
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "Spring Boot Workshop",
  "date": "2026-04-15T10:00:00",
  "capacity": 50
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "title": "Spring Boot Workshop",
  "date": "2026-04-15T10:00:00",
  "capacity": 50,
  "status": "OPEN"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8080/events \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot Workshop",
    "date": "2026-04-15T10:00:00",
    "capacity": 50
  }'
```

**Error Cases:**
```json
// Past date (400 Bad Request)
{
  "message": "Event date must be in the future",
  "status": 400,
  "timestamp": "2026-03-25T21:55:00Z"
}

// Non-admin user tries to create (403 Forbidden)
{
  "message": "Access Denied",
  "status": 403,
  "timestamp": "2026-03-25T21:55:00Z"
}

// Missing capacity field (400 Bad Request)
{
  "message": "capacity: Capacity is required",
  "status": 400,
  "timestamp": "2026-03-25T21:55:00Z"
}
```

---

### Get All Events (Public)

**Endpoint:** `GET /events`
**Authorization:** Not required
**Query Parameters:**
- `filter`: Optional - "upcoming", "past", or empty for all

**Request:**
```bash
curl http://localhost:8080/events
curl http://localhost:8080/events?filter=upcoming
curl http://localhost:8080/events?filter=past
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Spring Boot Workshop",
    "date": "2026-04-15T10:00:00",
    "capacity": 50,
    "status": "OPEN"
  },
  {
    "id": 2,
    "title": "Kubernetes Masterclass",
    "date": "2026-05-20T14:00:00",
    "capacity": 30,
    "status": "OPEN"
  }
]
```

---

### Get Event by ID (Public)

**Endpoint:** `GET /events/{id}`
**Authorization:** Not required

**Request:**
```bash
curl http://localhost:8080/events/1
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Spring Boot Workshop",
  "date": "2026-04-15T10:00:00",
  "capacity": 50,
  "status": "OPEN"
}
```

**Error (404 Not Found):**
```json
{
  "message": "Event not found with id: 999",
  "status": 404,
  "timestamp": "2026-03-25T21:55:00Z"
}
```

---

### Get Event Availability (Public)

**Endpoint:** `GET /events/{id}/availability`
**Authorization:** Not required

**Request:**
```bash
curl http://localhost:8080/events/1/availability
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Spring Boot Workshop",
  "capacity": 50,
  "registrationCount": 35,
  "availableSeats": 15
}
```

---

## 📝 User Registration

### Register for Event (USER/ADMIN)

**Endpoint:** `POST /register`
**Authorization:** `ROLE_USER` or `ROLE_ADMIN`
**Validation:**
- eventId: @NotNull, @Positive
- userId: @NotNull, @Positive

**Request Headers:**
```
Authorization: Bearer <accessToken>
Content-Type: application/json
```

**Request Body:**
```json
{
  "eventId": 1,
  "userId": 101
}
```

**Response (201 Created) - Registered:**
```json
{
  "state": "REGISTERED",
  "eventId": 1,
  "userId": 101,
  "registrationId": 201,
  "waitlistId": null,
  "message": "User registered successfully"
}
```

**Response (201 Created) - Waitlisted:**
```json
{
  "state": "WAITLISTED",
  "eventId": 1,
  "userId": 102,
  "registrationId": null,
  "waitlistId": 301,
  "message": "Event is full. User added to waitlist"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8080/register \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": 1,
    "userId": 101
  }'
```

**Error Cases:**
```json
// Event not found (404 Not Found)
{
  "message": "Event not found with id: 999",
  "status": 404,
  "timestamp": "2026-03-25T21:55:00Z"
}

// Already registered (409 Conflict)
{
  "message": "User is already registered for this event",
  "status": 409,
  "timestamp": "2026-03-25T21:55:00Z"
}

// Already in waitlist (409 Conflict)
{
  "message": "User is already in waitlist for this event",
  "status": 409,
  "timestamp": "2026-03-25T21:55:00Z"
}

// Event date passed (400 Bad Request)
{
  "message": "Event is closed because event date has passed",
  "status": 400,
  "timestamp": "2026-03-25T21:55:00Z"
}

// Missing userId (400 Bad Request)
{
  "message": "userId: User id is required",
  "status": 400,
  "timestamp": "2026-03-25T21:55:00Z"
}
```

---

### Cancel Registration (USER/ADMIN)

**Endpoint:** `DELETE /register/{id}`
**Authorization:** `ROLE_USER` or `ROLE_ADMIN`

**Request:**
```bash
curl -X DELETE http://localhost:8080/register/201 \
  -H "Authorization: Bearer <token>"
```

**Response (204 No Content):**
```
(Empty response body)
```

**Behavior:**
1. Cancels registration
2. Checks waitlist for next user
3. Auto-promotes first waitlisted user to registered
4. Updates event status if needed

**Error (404 Not Found):**
```json
{
  "message": "Registration not found with id: 999",
  "status": 404,
  "timestamp": "2026-03-25T21:55:00Z"
}
```

---

## 🔐 Authentication Error Cases

### Missing Authorization Header (401)
```bash
curl http://localhost:8080/register -X POST \
  -H "Content-Type: application/json" \
  -d '{"eventId": 1, "userId": 101}'
```

**Response (401 Unauthorized):**
```json
{
  "message": "Invalid username or password",
  "status": 401,
  "timestamp": "2026-03-25T21:55:00Z"
}
```

### Invalid Token (401)
```bash
curl http://localhost:8080/register -X POST \
  -H "Authorization: Bearer invalid-token" \
  -H "Content-Type: application/json" \
  -d '{"eventId": 1, "userId": 101}'
```

**Response:** No authentication in SecurityContext, request rejected.

### Insufficient Permissions (403)
```bash
# USER trying to create event
curl -X POST http://localhost:8080/events \
  -H "Authorization: Bearer <user-token>" \
  -H "Content-Type: application/json" \
  -d '{"title": "Event", "date": "2026-04-15T10:00:00", "capacity": 50}'
```

**Response (403 Forbidden):**
```json
{
  "message": "Access Denied",
  "status": 403,
  "timestamp": "2026-03-25T21:55:00Z"
}
```

---

## 🧪 Complete Testing Workflow

### Scenario 1: Admin Creates Event, User Registers

```bash
#!/bin/bash

# 1. Admin logs in
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"root","password":"Djaadhi_09"}' \
  | jq -r '.accessToken')

# 2. Admin creates event
EVENT=$(curl -s -X POST http://localhost:8080/events \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Kubernetes Workshop",
    "date": "2026-04-15T10:00:00",
    "capacity": 2
  }')

EVENT_ID=$(echo $EVENT | jq '.id')

# 3. User logs in
USER_TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"1234"}' \
  | jq -r '.accessToken')

# 4. User 1 registers
REG1=$(curl -s -X POST http://localhost:8080/register \
  -H "Authorization: Bearer $USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"eventId\": $EVENT_ID, \"userId\": 101}")

echo "User 1 registration: $(echo $REG1 | jq '.state')"

# 5. User 2 registers
REG2=$(curl -s -X POST http://localhost:8080/register \
  -H "Authorization: Bearer $USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"eventId\": $EVENT_ID, \"userId\": 102}")

echo "User 2 registration: $(echo $REG2 | jq '.state')"

# 6. User 3 tries to register (should be waitlisted)
REG3=$(curl -s -X POST http://localhost:8080/register \
  -H "Authorization: Bearer $USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"eventId\": $EVENT_ID, \"userId\": 103}")

echo "User 3 registration: $(echo $REG3 | jq '.state')"

# 7. Check availability
curl -s http://localhost:8080/events/$EVENT_ID/availability | jq
```

---

## 📊 Swagger UI Testing

1. Open `http://localhost:8080/swagger-ui/index.html`
2. Click **Authorize** button
3. Paste JWT token (with or without "Bearer " prefix)
4. Use endpoint forms to test API

**Example:**
- Login → Copy accessToken
- Click Authorize → Paste token
- Try `/events` POST
- Fill form: title, date (future), capacity (positive)
- Execute
- View response

---

## ⚠️ Common Errors & Solutions

| Error | Cause | Solution |
|-------|-------|----------|
| 401 Unauthorized | Missing/invalid token | Login first, get token |
| 403 Forbidden | Insufficient role | Use correct user (root=ADMIN) |
| 400 Bad Request | Invalid data | Check date is future, capacity > 0 |
| 404 Not Found | Resource doesn't exist | Check event/registration ID |
| 409 Conflict | User already registered | Check registration status |

---

## 🔄 Request/Response Flow Diagram

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
       ├─ POST /auth/login ──────────────┐
       │  {"username": "root",            │
       │   "password": "Djaadhi_09"}      │
       │                                  ▼
       │                          ┌──────────────────┐
       │                          │ AuthController   │
       │                          │ ├─ Validate creds│
       │                          │ ├─ Get role      │
       │                          │ ├─ Create JWT    │
       │                          └────────┬─────────┘
       │◀───────────────────────────────────┤
       │  {"accessToken": "...",            │
       │   "tokenType": "Bearer"}           │
       │                                    │
       ├─ POST /events ──────────────────────┐
       │  Authorization: Bearer <token>      │
       │  {"title": "...",                   │
       │   "date": "...",                    │
       │   "capacity": ...}                  │
       │                                     ▼
       │                             ┌──────────────────┐
       │                             │ JwtAuthFilter    │
       │                             │ ├─ Extract token │
       │                             │ ├─ Validate      │
       │                             │ ├─ Get role      │
       │                             │ └─ Add to context│
       │                             └────────┬─────────┘
       │                                      ▼
       │                             ┌──────────────────┐
       │                             │ @PreAuthorize    │
       │                             │ (hasRole ADMIN)  │
       │                             └────────┬─────────┘
       │                                      ▼
       │                             ┌──────────────────┐
       │                             │ EventController  │
       │                             │ ├─ Validate DTO  │
       │                             │ ├─ Create event  │
       │                             │ └─ Return DTO    │
       │                             └────────┬─────────┘
       │◀────────────────────────────────────┤
       │  {"id": 1, "title": "...", ...}    │
       │  (201 Created)                      │
       │                                    │
       └────────────────────────────────────┘
```

---

## 📱 Database Verification

Check if registrations were saved:

```sql
SELECT r.id, r.event_id, r.user_id, e.title, e.status 
FROM registrations r 
JOIN events e ON r.event_id = e.id 
ORDER BY r.id DESC;
```

Check waitlist:

```sql
SELECT w.id, w.event_id, w.user_id, e.title, w.created_at 
FROM waitlist w 
JOIN events e ON w.event_id = e.id 
ORDER BY w.created_at ASC;
```

---

## 🚀 Performance Tips

1. **Token Reuse:** Don't login for every request, reuse token
2. **Pagination:** Use filters for large event lists
3. **Caching:** Swagger UI caches responses, refresh if needed
4. **Connection Pool:** Keep JDBC connections pooled (configured)
5. **Transactions:** All writes are atomic, no race conditions

