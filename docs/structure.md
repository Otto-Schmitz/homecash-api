# structure.md

## Package Structure

src/main/java/com/app/homecash/
  controller/
    HouseController.java
    ExpenseController.java

  service/
    HouseService.java
    ExpenseService.java
    impl/
      HouseServiceImpl.java
      ExpenseServiceImpl.java

  domain/
    User.java
    House.java
    HouseMember.java
    Expense.java
    ExpenseParticipant.java
    CreditCard.java
    Invoice.java

  repository/
    UserRepository.java
    HouseRepository.java
    ExpenseRepository.java
    CreditCardRepository.java
    InvoiceRepository.java

  dto/
    request/
    response/

  mapper/
  config/

## Rules
- Controllers depend only on service interfaces.
- Services depend on repositories and other service interfaces.
- Domain has no dependency on Spring.
- Repositories only extend JpaRepository.
- DTOs are the only objects exposed to controllers.
- Lombok is mandatory in domain and DTOs.
