# style.md

## Code Style
- Classes: PascalCase
- Methods: camelCase
- Constants: UPPER_SNAKE_CASE
- Variables/Objects: camelCase

## Lombok
Use in:
- Domain
- DTOs
- Entities

Prefer:
- @Getter @Setter
- @Builder
- @NoArgsConstructor
- @AllArgsConstructor
- @Data

## Interfaces
- Every service must have an interface.
- Implementation always in service.impl.

## Repositories
- Extend JpaRepository.
- No custom logic unless strictly query-related.

## Commits
Must be clean and short:
- feat: (new feature description)
- fix: (bug fix description)
- refactor: (refactor only description)
- docs: (documentation description)

## Mappers
Must use Builder.
