# architecture.md

## Architecture Style
Layered Architecture (N-Tier).

## Layers

### Controller Layer
- REST endpoints
- Request/response mapping
- No business logic

### Service Layer
- Business rules
- Orchestrates flows
- Exposed only through interfaces

### Domain Layer
- Entities and value rules
- Validation of invariants

### Repository Layer
- Data access using JpaRepository
- No business logic

### DTO Layer
- Input/output contracts
- Never expose entities directly

## Flow
Request  
→ Controller  
→ Service Interface  
→ Service Implementation  
→ Repository  
→ Database
