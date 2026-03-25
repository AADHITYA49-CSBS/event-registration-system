# Event Registration System

Spring Boot API for events and registrations with JWT-based stateless authentication.

## JWT Login

- Endpoint: `POST /auth/login`
- Request body:

```json
{
  "username": "demo-user"
}
```

- Success response:

```json
{
  "token": "<jwt-token>",
  "type": "Bearer"
}
```

## Use Token in Swagger / API Calls

Send the token in the `Authorization` header:

```text
Authorization: Bearer <jwt-token>
```

## Current Auth Behavior (Dev)

This implementation currently validates only `username` in the login request and issues a token.
Password validation and user store integration can be added in a follow-up change.

