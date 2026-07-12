# IPL Match Ticket Booking System

A Spring Boot REST API for booking IPL match tickets. Admins manage stadiums, matches, and ticket pricing; users browse matches and book seats. Access is role-based, request handling is centralized, and every service call is logged automatically.

## Features

- **Match & stadium management** — create, update, delete, and list matches; register stadiums.
- **Ticket booking** — users book a given number of seats for a match and get back a booking summary with total amount.
- **Role-based access control** — `ADMIN` and `USER` roles, enforced at the security-filter level (admins manage matches/stadiums, users view matches and book tickets).
- **Authentication** — JWT-based login with refresh tokens, plus OAuth2 social login (Google, GitHub).
- **Centralized exception handling** — dedicated exceptions for a missing match, a missing stadium, and a stadium at full capacity, all mapped to consistent error responses.
- **Request logging via AOP** — an `@Aspect` wraps every service method, logging entry, exit, and execution time.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot |
| Security | Spring Security, JWT (JJWT), OAuth2 Client |
| Persistence | Spring Data JPA, PostgreSQL |
| Logging | Spring AOP |
| Build | Maven |

## Entities

- **Match** — date, stadium, team A, team B, ticket price.
- **Stadium** — name, location, capacity, hosted matches.
- **Ticket Booking** — user, match, number of seats, total amount.

## Roles & Access

| Action | Role |
|---|---|
| Create / update / delete a match | ADMIN |
| Register a stadium | ADMIN |
| View matches | ADMIN, USER |
| Book a ticket | USER |

## API Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/v1/users/register` | Public | Register a user |
| `POST` | `/api/v1/admin/register` | Admin | Register an admin |
| `POST` | `/api/v1/login` | Public | Log in, get access + refresh token |
| `POST` | `/api/v1/refresh` | Public | Refresh an access token |
| `POST` | `/api/v1/stadiums` | Admin | Add a stadium |
| `POST` | `/api/v1/matches` | Admin | Create a match |
| `PUT` | `/api/v1/matches/{id}` | Admin | Update a match |
| `DELETE` | `/api/v1/matches/{id}` | Admin | Delete a match |
| `GET` | `/api/v1/matches` | Admin, User | List all matches |
| `GET` | `/api/v1/matches/{id}` | Admin, User | Get a match by ID |
| `POST` | `/api/v1/tickets/{userId}/{matchId}/{seats}` | User | Book tickets for a match |

## Error Handling

| Exception | Meaning |
|---|---|
| `MatchNotFoundException` | No match exists for the given ID |
| `StadiumNotFoundException` | No stadium exists for the given ID |
| `StadiumFullException` | Requested seats exceed remaining stadium capacity |

## Getting Started

```bash
# 1. Clone the repository
git clone <this-repository-url>
cd ipl-match-ticket-booking-system

# 2. Create the database
psql -U postgres -c "CREATE DATABASE ipl_ticket_db;"

# 3. Set required environment variables
export jwt_SECRET=your_jwt_secret
export GOOGLE_CLIENT_ID=your_google_client_id
export GOOGLE_CLIENT_SECRET=your_google_client_secret
export GITHUB_CLIENT_ID=your_github_client_id
export GITHUB_CLIENT_SECRET=your_github_client_secret

# 4. Run the application
./mvnw spring-boot:run
```

The API is available at `http://localhost:8080/api/v1`.

## Configuration

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ipl_ticket_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

jwt.SECRET=${jwt_SECRET}
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}
```

Never commit real secrets — use environment variables or a local, git-ignored properties file.
