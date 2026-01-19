# .cursor/rules.md — Backend Only

Scope:
These rules apply to all files under /backend.

Source of truth:
Always follow:
- /docs/rules.md
- /docs/architecture.md
- /docs/structure.md
- /docs/domain.md
- /docs/apis.md
- /docs/style.md
- /docs/prompts.md

Architecture:
- Layered Architecture (N-Tier).
- Controllers → only service interfaces.
- Services → interfaces + impl.
- Repositories → JpaRepository only.
- Domain → no Spring imports.

Coding Rules:
- No business logic in controllers.
- Business logic in services and domain.
- DTOs for all inputs/outputs.
- Never expose entities in controllers.
- Always filter by house (multi-tenant).

Lombok:
- Mandatory in domain and DTOs.
- Prefer:
  - @Getter @Setter
  - @Builder
  - @NoArgsConstructor
  - @AllArgsConstructor

Persistence:
- Use JpaRepository.
- No custom logic in repository unless query-related.
- Soft delete everywhere.
- Money in cents (int/long).
- Dates in UTC.

Security:
- Every endpoint authenticated.
- Always validate:
  - User belongs to house.
  - User has permission.

Generation Rules:
When generating code:
1. Read docs/backend first.
2. Respect package structure from 03-structure.md.
3. Create in order:
   - Domain
   - Repository
   - Service interface
   - Service impl
   - DTOs
   - Mapper
   - Controller
4. Never skip layers.
5. No shortcuts or mixed responsibilities.

If any instruction conflicts:
Priority order:
1. This file
2. docs/rules.md
3. docs/architecture.md
4. docs/structure.md
5. docs/domain.md
6. docs/style.md
