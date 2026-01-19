package com.app.homecash.repository;

import com.app.homecash.domain.InvoiceExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceExpenseRepository extends JpaRepository<InvoiceExpense, Long> {

    List<InvoiceExpense> findByInvoiceId(Long invoiceId);

    List<InvoiceExpense> findByExpenseId(Long expenseId);
}

