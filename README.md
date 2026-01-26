# HomeCash API

HomeCash is a backend application for managing shared household expenses, designed to help families and roommates track and split costs efficiently. The system supports multiple houses, expense tracking, credit card management, and invoice generation.

## Overview

HomeCash provides a RESTful API built with Spring Boot that enables users to:
- Create and manage shared houses (homes)
- Track expenses with multiple participants
- Manage credit cards and generate invoices
- Split expenses among house members
- Monitor payment status and overdue expenses

## Technology Stack

- **Java 21** - Programming language
- **Spring Boot 3.2.0** - Application framework
- **Spring Data JPA** - Data persistence
- **Spring Security** - Authentication and authorization
- **JWT (jjwt 0.12.3)** - Token-based authentication
- **H2 Database** - In-memory database for development
- **Maven** - Build automation
- **Lombok** - Boilerplate code reduction
- **Jakarta Validation** - Input validation

## Architecture

The application follows a **Layered Architecture (N-Tier)** pattern with clear separation of concerns:

### Layers

1. **Controller Layer** - REST endpoints, request/response mapping, no business logic
2. **Service Layer** - Business rules and orchestration, exposed through interfaces
3. **Domain Layer** - Entities and domain rules, no Spring dependencies
4. **Repository Layer** - Data access using JpaRepository, no business logic
5. **DTO Layer** - Input/output contracts, entities never exposed directly
6. **Mapper Layer** - Conversion between DTOs and domain entities

### Request Flow

```
Request → Controller → Service Interface → Service Implementation → Repository → Database
```

## Project Structure

```
src/main/java/com/app/homecash/
├── controller/          # REST controllers
├── service/            # Service interfaces
│   └── impl/          # Service implementations
├── domain/             # JPA entities and domain models
├── repository/         # JPA repositories
├── dto/               # Data Transfer Objects
│   ├── request/       # Request DTOs
│   └── response/      # Response DTOs
├── mapper/            # DTO-Entity mappers
└── config/            # Configuration classes (Security, JWT, etc.)
```

## Features

### Authentication
- User registration with email and CPF validation
- JWT-based authentication
- Password encryption using BCrypt
- Token-based authorization for all protected endpoints

### House Management
- Create and manage shared houses
- Invite members via invite codes
- Role-based access (OWNER, MEMBER)
- Multi-tenant data isolation

### Expense Tracking
- Create expenses with multiple participants
- Support for different expense types (FIXED, FLEXIBLE, ONE_TIME)
- Multiple payment methods (CASH, PIX, BOLETO, DEBIT, CREDIT)
- Track payment status (OPEN, PAID, OVERDUE)
- Split expenses among participants

### Credit Card Management
- Register credit cards with limits and billing cycles
- Track closing and due dates
- Link expenses to credit cards

### Invoice Management
- Generate invoices from credit card expenses
- Track invoice status (OPEN, CLOSED, PAID)
- Automatic expense status update when invoice is paid

## API Endpoints

### Authentication
- `POST /auth/register` - Register a new user
- `POST /auth/login` - Authenticate and receive JWT token

### Houses
- `POST /houses` - Create a new house
- `GET /houses` - List all houses for authenticated user
- `GET /houses/{id}` - Get house details
- `POST /houses/{id}/invite` - Generate new invite code
- `POST /houses/join` - Join a house using invite code

### Expenses
- `POST /houses/{id}/expenses` - Create a new expense
- `GET /houses/{id}/expenses` - List expenses for a house
- `GET /expenses/{id}` - Get expense details
- `PUT /expenses/{id}` - Update an expense
- `DELETE /expenses/{id}` - Delete an expense
- `POST /expenses/{id}/pay` - Mark expense as paid

### Credit Cards
- `POST /cards` - Register a new credit card
- `GET /cards` - List all credit cards for authenticated user
- `GET /cards/{id}` - Get credit card details
- `PUT /cards/{id}` - Update credit card
- `DELETE /cards/{id}` - Delete credit card

### Invoices
- `GET /cards/{id}/invoices` - List invoices for a credit card
- `POST /invoices/{id}/pay` - Mark invoice as paid

## Domain Model

### User
Represents application users with unique email and CPF. Supports soft delete and can belong to multiple houses.

### House
Represents a shared living space. Must always have at least one member and one owner. Uses invite codes for member management.

### HouseMember
Links users to houses with roles (OWNER, MEMBER). Enforces ownership rules and permissions.

### Expense
Represents a shared expense with amount, category, type, payment method, and status. Can have multiple participants.

### ExpenseParticipant
Links users to expenses with individual amounts and payment status. Sum of participant amounts must equal expense total.

### CreditCard
Represents a credit card with limit, billing cycle (closing day, due day), and brand information.

### Invoice
Represents a credit card invoice for a specific month/year. Links multiple credit expenses and tracks payment status.

## Security

- All endpoints except `/auth/**` and `/h2-console/**` require JWT authentication
- JWT tokens are validated on every request via `JwtAuthenticationFilter`
- User ownership and house membership are validated for all operations
- Multi-tenant data isolation enforced at service layer
- Passwords are encrypted using BCrypt

## Configuration

### Application Properties

The application uses `application.yml` for configuration:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:homecashdb
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: your-secret-key-here
  expiration: 86400000  # 24 hours in milliseconds
```

### JWT Configuration

- **Secret Key**: Configure `jwt.secret` in `application.yml` (use a strong random secret in production)
- **Expiration**: Default is 24 hours (86400000 ms), configurable via `jwt.expiration`

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

### Building the Project

```bash
mvn clean install
```

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### H2 Console

For development, H2 console is available at `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:homecashdb`
- Username: `sa`
- Password: (empty)

## Development Guidelines

### Code Style

- Controllers depend only on service interfaces
- Services depend on repositories and other service interfaces
- Domain entities have no Spring dependencies
- Repositories only extend JpaRepository
- DTOs are the only objects exposed to controllers
- Lombok is mandatory in domain and DTOs

### Validation

- Input validation using Jakarta Bean Validation
- Domain rules enforced in service layer
- Multi-tenant security checks in all operations

### Error Handling

- Business rule violations throw `IllegalArgumentException` or `IllegalStateException`
- Authentication failures return appropriate HTTP status codes
- Validation errors return 400 Bad Request

## License

This project is part of the HomeCash application suite.

