# rules.md

## General Principles
- Business logic never goes in controllers.
- Controllers only:
  - Receive HTTP requests
  - Validate input format
  - Call service interfaces
  - Return responses
- Business rules live in:
  - Domain
  - Service layer
- Persistence is a technical detail.
- No business logic inside framework annotations.

## Layer Rules
- controller → talks only to service interfaces
- service → talks to repositories and other service interfaces
- repository → only data access
- domain → pure business objects

## Security
- Every endpoint requires authentication.
- Every action must validate:
  - User belongs to the house
  - User has permission
- Always filter data by house.

## Data
- Soft delete everywhere.
- No `select *`.
- Dates in UTC.
- Money stored as integer (cents).
