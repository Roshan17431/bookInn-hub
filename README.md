# BookInn Hub

BookInn Hub is a full-stack hotel booking platform built with a Spring Boot REST API and a Vite React frontend. Guests can browse rooms, search availability by date and room type, create bookings, view their profile and booking history, and look up reservations by confirmation code. Admin users can manage rooms, bookings, and users.

## Highlights

- Guest registration and JWT-based login
- Role-based access for `USER` and `ADMIN`
- Room catalog with image upload support through AWS S3
- Availability search with date-range conflict checks
- Booking creation, cancellation, and confirmation-code lookup
- User profile dashboard with booking history
- Admin dashboards for rooms, bookings, and users
- Configurable frontend-to-backend API URL
- Configurable CORS for local and deployed frontends
- Docker and Render deployment support

## Tech Stack

| Layer | Technology |
| --- | --- |
| Frontend | React 19, TypeScript, Vite, React Router, Lucide Icons |
| Backend | Java 21, Spring Boot 3.5.9 |
| Security | Spring Security, JWT |
| Database | PostgreSQL in production, H2 for tests |
| ORM | Spring Data JPA / Hibernate |
| File Storage | AWS S3 |
| Monitoring | Spring Actuator, Micrometer Prometheus |
| Build | Maven Wrapper, npm |
| Deployment | Render backend, Vercel frontend |

## Repository Layout

```text
.
├── frontend/                         # Vite React app
│   ├── src/
│   │   ├── components/               # Navbar, footer, room cards, modals
│   │   ├── context/                  # Auth context
│   │   ├── pages/                    # Public, user, and admin screens
│   │   ├── services/                 # API client
│   │   └── types/                    # Shared frontend DTO types
│   ├── .env.example
│   └── vercel.json
├── src/main/java/com/roshan/bookInn_hub/
│   ├── config/                       # Security and CORS
│   ├── controller/                   # REST API controllers
│   ├── dto/                          # API response DTOs
│   ├── entity/                       # JPA entities
│   ├── repository/                   # Spring Data repositories
│   ├── security/                     # JWT filter and utilities
│   └── service/                      # Business logic
├── src/main/resources/
│   └── application.properties
├── src/test/                         # Spring Boot context test config
├── Dockerfile
├── render.yaml
└── pom.xml
```

## Prerequisites

- Java 21 or newer
- Node.js 20 or newer
- npm
- PostgreSQL database
- AWS S3 bucket and credentials
- Docker, optional

## Environment Variables

Create a `.env` file in the project root for the backend. The app also supports normal system environment variables, which is what Render uses.

```dotenv
DB_URL=jdbc:postgresql://<host>:<port>/<database>
DB_USER=<database-user>
DB_PASSWORD=<database-password>
JWT_SECRET=<long-random-secret-at-least-32-bytes>
AWS_ACCESS_KEY=<aws-access-key-id>
AWS_SECRET_KEY=<aws-secret-access-key>
BUCKET_NAME=<s3-bucket-name>
APP_CORS_ALLOWED_ORIGIN_PATTERNS=http://localhost:3000,https://book-inn-hub.vercel.app,https://*.vercel.app
```

Create `frontend/.env` for local frontend development:

```dotenv
VITE_API_BASE_URL=http://localhost:8080
```

`frontend/.env.example` contains the same local default. In production, `frontend/vercel.json` sets `VITE_API_BASE_URL` to the Render backend URL.

## Running Locally

Clone and start the backend:

```bash
git clone https://github.com/Roshan17431/bookInn-hub.git
cd bookInn-hub
./mvnw spring-boot:run
```

Start the frontend in a second terminal:

```bash
cd frontend
npm ci
npm run dev
```

Local URLs:

- Frontend: `http://localhost:3000`
- Backend API: `http://localhost:8080`
- Health check: `http://localhost:8080/actuator/health`

## Build and Test

Backend:

```bash
./mvnw test
./mvnw -DskipTests package
```

Frontend:

```bash
cd frontend
npm ci
npx tsc --noEmit
npm run build
npm audit --omit=dev
```

## Docker

Build and run the backend container:

```bash
docker build -t bookinn-hub .
docker run -p 8080:8080 --env-file .env bookinn-hub
```

The Dockerfile packages only the Spring Boot backend. The frontend is intended to be built and hosted separately, for example on Vercel.

## Deployment

### Backend on Render

The repository includes `render.yaml` for a Docker-based Render service.

Required Render environment variables:

```text
DB_URL
DB_USER
DB_PASSWORD
JWT_SECRET
AWS_ACCESS_KEY
AWS_SECRET_KEY
BUCKET_NAME
APP_CORS_ALLOWED_ORIGIN_PATTERNS
SERVER_PORT
```

`SERVER_PORT` is set to `8080` in `render.yaml`. If Render provides `PORT`, the application also supports that.

### Frontend on Vercel

The frontend lives in `frontend/`.

Vercel settings:

- Framework preset: Vite
- Build command: `npm run build`
- Output directory: `dist`
- Root directory: `frontend`
- Environment variable: `VITE_API_BASE_URL=https://bookinn-hub.onrender.com`

## API Overview

Protected routes require:

```http
Authorization: Bearer <jwt-token>
```

### Authentication

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `POST` | `/auth/register` | Public | Register a new user. Role is always created as `USER`. |
| `POST` | `/auth/login` | Public | Login and receive a JWT token. |

### Users

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `GET` | `/users/all` | Admin | List all users. |
| `GET` | `/users/get-by-id/{userId}` | Admin | Get one user by id. |
| `DELETE` | `/users/delete/{userId}` | Admin | Delete a user. |
| `GET` | `/users/get-logged-in-profile-info` | Authenticated | Get the current user's profile. |
| `GET` | `/users/get-user-booking/{userId}` | Same user or admin | Get booking history for a user. |

### Rooms

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `GET` | `/rooms/all` | Public | List all rooms. |
| `GET` | `/rooms/types` | Public | List distinct room types. |
| `GET` | `/rooms/room-by-id/{roomId}` | Public | Get one room. |
| `GET` | `/rooms/all-available-rooms` | Public | List rooms with no bookings. |
| `GET` | `/rooms/available-rooms-by-date-and-type` | Public | Search availability by `checkInDate`, `checkOutDate`, and `roomType`. |
| `POST` | `/rooms/add` | Admin | Add a room with optional photo upload. |
| `PUT` | `/rooms/update/{roomId}` | Admin | Update room details and optional photo. |
| `DELETE` | `/rooms/delete/{roomId}` | Admin | Delete a room. |

### Bookings

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `POST` | `/bookings/book-room/{roomId}/{userId}` | Same user or admin | Create a booking. |
| `GET` | `/bookings/all` | Admin | List all bookings. |
| `GET` | `/bookings/get-by-confirmation-code/{confirmationCode}` | Public | Find a booking by confirmation code. |
| `DELETE` | `/bookings/cancel/{bookingId}` | Booking owner or admin | Cancel a booking. |

## Request Examples

Register:

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Demo User",
    "email": "demo@example.com",
    "password": "password123",
    "phoneNumber": "9999999999"
  }'
```

Login:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "demo@example.com",
    "password": "password123"
  }'
```

Search rooms:

```bash
curl "http://localhost:8080/rooms/available-rooms-by-date-and-type?checkInDate=2026-08-01&checkOutDate=2026-08-03&roomType=Single"
```

Book a room:

```bash
curl -X POST http://localhost:8080/bookings/book-room/1/1 \
  -H "Authorization: Bearer <jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "checkInDate": "2026-08-01",
    "checkOutDate": "2026-08-03",
    "numOfAdults": 2,
    "numOfChildren": 0
  }'
```

## Security Notes

- JWTs are sent through the `Authorization` header.
- Public users cannot create admin accounts through registration.
- Booking creation and cancellation are checked against the authenticated user.
- Admin-only endpoints are protected with Spring method security.
- CORS origins should be restricted in production with `APP_CORS_ALLOWED_ORIGIN_PATTERNS`.
- The environment-printer dependency is disabled by default through configuration.

## Troubleshooting

### Frontend cannot reach backend

Check `frontend/.env`:

```dotenv
VITE_API_BASE_URL=http://localhost:8080
```

Restart the Vite dev server after changing environment variables.

### CORS errors in browser

Make sure the frontend origin is listed in:

```dotenv
APP_CORS_ALLOWED_ORIGIN_PATTERNS=http://localhost:3000,https://your-frontend-domain.com
```

### Backend fails to start because of missing datasource

Confirm `DB_URL`, `DB_USER`, and `DB_PASSWORD` are set and that the database is reachable.

### Room image upload fails

Confirm `AWS_ACCESS_KEY`, `AWS_SECRET_KEY`, and `BUCKET_NAME` are valid and the IAM user has permission to upload objects to the bucket.

## Quality Checklist

Before opening a pull request or deploying:

```bash
./mvnw test
cd frontend
npx tsc --noEmit
npm run build
npm audit --omit=dev
```

## License

This project is provided for learning and demonstration purposes. Add a formal license before using it in production or distributing it publicly.
