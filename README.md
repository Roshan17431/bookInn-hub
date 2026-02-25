# BookInn Hub

A hotel room booking management REST API built with Spring Boot. It supports user registration and authentication (including OAuth2 social login), room management with photo uploads, and a full booking lifecycle with confirmation codes.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.9 |
| Security | Spring Security + JWT, OAuth2 (Google, GitHub) |
| Database | PostgreSQL (primary) / MySQL |
| ORM | Spring Data JPA (Hibernate) |
| File Storage | AWS S3 |
| Monitoring | Spring Actuator + Prometheus (Micrometer) |
| Build | Maven |
| Container | Docker |
| Deployment | Render |

## Features

- **User management** – register, login, view profile and booking history
- **Room catalog** – add, update, delete rooms with photos, prices, and room types
- **Booking system** – book rooms by date range, cancel bookings, look up by confirmation code
- **JWT authentication** – stateless, token-based API security
- **Social login** – OAuth2 integration with Google and GitHub
- **Cloud storage** – room photos stored in AWS S3
- **Observability** – health checks and Prometheus metrics via Spring Actuator

## Prerequisites

- Java 21+
- Maven 3.9+
- A PostgreSQL (or MySQL) database
- An AWS S3 bucket
- (Optional) Docker

## Environment Variables

Create a `.env` file in the project root (the app uses `spring-dotenv` to load it):

```dotenv
DB_URL=jdbc:postgresql://<host>:<port>/<database>
DB_USER=<db-username>
DB_PASSWORD=<db-password>
JWT_SECRET=<long-random-secret>
AWS_ACCESS_KEY=<aws-access-key-id>
AWS_SECRET_KEY=<aws-secret-access-key>
BUCKET_NAME=<s3-bucket-name>
```

For OAuth2 social login, add the following to `application.properties` or your environment:

```properties
spring.security.oauth2.client.registration.google.client-id=<google-client-id>
spring.security.oauth2.client.registration.google.client-secret=<google-client-secret>
spring.security.oauth2.client.registration.github.client-id=<github-client-id>
spring.security.oauth2.client.registration.github.client-secret=<github-client-secret>
```

## Running Locally

```bash
# Clone the repository
git clone https://github.com/Roshan17431/bookInn-hub5.git
cd bookInn-hub5

# Build and run
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## Running with Docker

```bash
# Build the image
docker build -t bookinn-hub .

# Run (pass environment variables)
docker run -p 8080:8080 --env-file .env bookinn-hub
```

## API Endpoints

### Authentication – `/auth`

| Method | Path | Description | Auth required |
|---|---|---|---|
| POST | `/auth/register` | Register a new user | No |
| POST | `/auth/login` | Login and receive a JWT token | No |

### Users – `/users`

| Method | Path | Description | Auth required |
|---|---|---|---|
| GET | `/users/all` | List all users | Admin |
| GET | `/users/get-by-id/{userId}` | Get user by ID | Admin |
| DELETE | `/users/get-by-id/{userId}` | Delete user by ID (note: path mirrors the GET route in the source code) | Admin |
| GET | `/users/get-logged-in-profile-info` | Get current user's profile | User |
| GET | `/users/get-user-booking/{userId}` | Get bookings for a user | User |

### Rooms – `/rooms`

| Method | Path | Description | Auth required |
|---|---|---|---|
| POST | `/rooms/add` | Add a new room (with photo) | Admin |
| GET | `/rooms/all` | List all rooms | No |
| GET | `/rooms/types` | List all room types | No |
| GET | `/rooms/room-by-id/{roomId}` | Get room by ID | No |
| GET | `/rooms/all-available-rooms` | List all available rooms | No |
| GET | `/rooms/available-rooms-by-date-and-type` | Search available rooms by date and type | No |
| PUT | `/rooms/update/{roomId}` | Update a room | Admin |
| DELETE | `/rooms/delete/{roomId}` | Delete a room | Admin |

### Bookings – `/bookings`

| Method | Path | Description | Auth required |
|---|---|---|---|
| POST | `/bookings/book-room/{roomId}/{userId}` | Book a room | User |
| GET | `/bookings/all` | List all bookings | Admin |
| GET | `/bookings/get-by-confirmation-code/{confirmationCode}` | Look up a booking | User |
| DELETE | `/bookings/cancel/{bookingId}` | Cancel a booking | User |

> **Note:** Include the JWT token in the `Authorization: Bearer <token>` header for all protected endpoints.

## Project Structure

```
src/main/java/com/roshan/bookInn_hub/
├── controller/          REST controllers
├── service/
│   ├── interfac/        Service interfaces
│   └── implementation/  Service implementations (including AwsS3Service)
├── entity/              JPA entities (User, Room, Booking)
├── dto/                 Data Transfer Objects and request/response models
├── repository/          Spring Data JPA repositories
├── config/              Security and CORS configuration
├── security/            JWT utilities and authentication filter
└── exception/           Custom exceptions
```

## Deployment

The project includes a `render.yaml` for one-click deployment to [Render](https://render.com). Set the required environment variables in the Render dashboard and trigger a deploy.

## License

This project is provided as-is for demonstration purposes.
