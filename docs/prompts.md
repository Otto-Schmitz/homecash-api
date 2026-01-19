# prompts.md

## Create Domain Entity
Create a domain entity using Lombok with fields and business validations based on domain.md rules.

## Create Service Interface
Create a service interface with methods for this use case, no implementation.

## Create Service Implementation
Implement this service interface using repositories and domain rules. No controller logic.

## Create Controller
Create a Spring REST controller that uses only the service interface, mapping DTOs.

## Create Repository
Create a JpaRepository for this entity with only query methods.

## Create DTOs
Create request and response DTOs using Lombok, no business logic.

## Create Mapper
Create a mapper to convert between DTOs and domain entities.

## Create Test
Create unit tests for service implementation, mocking repositories.
