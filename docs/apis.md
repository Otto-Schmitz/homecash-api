# apis.md

## Auth
- POST /auth/login
- POST /auth/register

## House
- POST /houses
- GET /houses
- GET /houses/{id}
- POST /houses/{id}/invite
- POST /houses/join

## Expense
- POST /houses/{id}/expenses
- GET /houses/{id}/expenses
- GET /expenses/{id}
- PUT /expenses/{id}
- DELETE /expenses/{id}
- POST /expenses/{id}/pay

## Credit Card
- POST /cards
- GET /cards
- PUT /cards/{id}
- DELETE /cards/{id}

## Invoice
- GET /cards/{id}/invoices
- POST /invoices/{id}/pay

Each endpoint must define:
- Input DTO
- Output DTO
- Error responses
