# domain.md

## User
Fields:
- id
- name
- email
- cpf
- phone_number
- birth_date
- active
- createdAt
- updatedAt
- disabledAt

Rules:
- Email must be unique.
- Cpf must be unique.
- Cannot be removed if it is the only owner of a house.
- Can have N Houses.

## House
Fields:
- id
- name
- inviteCode
- createdAt

Rules:
- Must always have at least one member.
- Must always have one owner.

## HouseMember
Fields:
- userId
- houseId
- role: OWNER | MEMBER

Rules:
- Only owner can remove members.
- Owner cannot remove himself if he is the only owner.
- Only owner can delete Houses.

## Expense
Fields:
- id
- houseId
- title
- category
- amountCents
- type: FIXED | FLEXIBLE | ONE_TIME
- createdAt
- dueDate
- paymentMethod: CASH | PIX | BOLETO | DEBIT | CREDIT
- status: OPEN | PAID | OVERDUE
- createdBy

Rules:
- Amount > 0.
- If payment method = CREDIT, must have credit card.
- Cannot be marked as paid without participants.

## ExpenseParticipant
Fields:
- expenseId
- userId
- amountCents
- status: OWES | PAID

Rules:
- Sum of participants = expense total.

## CreditCard
Fields:
- id
- userId
- name
- brand
- lastDigits
- limitCents
- closingDay
- dueDay

Rules:
- Only owner can edit or remove.

## Invoice
Fields:
- id
- creditCardId
- month
- year
- totalCents
- status: OPEN | CLOSED | PAID
- expsenseId[]

Rules:
- Only credit expenses can belong to an invoice.
- Paying invoice marks all linked expenses as paid.
- Expenses must be itemized.
- Must correspond to the expenses of some valid house.
