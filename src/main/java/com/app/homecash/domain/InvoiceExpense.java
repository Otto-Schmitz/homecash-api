package com.app.homecash.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invoice_expenses", indexes = {
    @Index(name = "idx_invoice_expense_invoice", columnList = "invoiceId"),
    @Index(name = "idx_invoice_expense_expense", columnList = "expenseId"),
    @Index(name = "idx_invoice_expense_unique", columnList = "invoiceId,expenseId", unique = true)
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "invoice_id", nullable = false)
    private Long invoiceId;

    @NotNull
    @Column(name = "expense_id", nullable = false)
    private Long expenseId;
}

