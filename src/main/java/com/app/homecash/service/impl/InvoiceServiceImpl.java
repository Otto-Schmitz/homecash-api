package com.app.homecash.service.impl;

import com.app.homecash.domain.*;
import com.app.homecash.dto.response.InvoiceResponse;
import com.app.homecash.mapper.InvoiceMapper;
import com.app.homecash.repository.*;
import com.app.homecash.service.CreditCardService;
import com.app.homecash.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceExpenseRepository invoiceExpenseRepository;
    private final ExpenseRepository expenseRepository;
    private final CreditCardRepository creditCardRepository;
    private final CreditCardService creditCardService;
    private final InvoiceMapper invoiceMapper;

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getByCreditCard(Long cardId, Long userId) {
        // Validate user owns the credit card (multi-tenant)
        creditCardService.getById(cardId, userId);

        List<Invoice> invoices = invoiceRepository.findByCreditCardId(cardId);
        return invoices.stream()
            .map(invoice -> {
                List<Long> expenseIds = invoiceExpenseRepository.findByInvoiceId(invoice.getId())
                    .stream()
                    .map(InvoiceExpense::getExpenseId)
                    .collect(Collectors.toList());
                return invoiceMapper.toResponse(invoice, expenseIds);
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getById(Long invoiceId, Long userId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new IllegalArgumentException("Invoice not found with id: " + invoiceId));

        // Validate user owns the credit card
        creditCardService.getById(invoice.getCreditCardId(), userId);

        List<Long> expenseIds = invoiceExpenseRepository.findByInvoiceId(invoiceId)
            .stream()
            .map(InvoiceExpense::getExpenseId)
            .collect(Collectors.toList());

        return invoiceMapper.toResponse(invoice, expenseIds);
    }

    @Override
    @Transactional
    public InvoiceResponse markAsPaid(Long invoiceId, Long userId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new IllegalArgumentException("Invoice not found with id: " + invoiceId));

        // Validate user owns the credit card
        creditCardService.getById(invoice.getCreditCardId(), userId);

        // Domain rule: Cannot mark as paid if already paid
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException("Invoice is already paid");
        }

        // Domain rule: Paying invoice marks all linked expenses as paid
        List<InvoiceExpense> invoiceExpenses = invoiceExpenseRepository.findByInvoiceId(invoiceId);
        
        if (invoiceExpenses.isEmpty()) {
            throw new IllegalStateException("Invoice has no expenses to mark as paid");
        }

        for (InvoiceExpense invoiceExpense : invoiceExpenses) {
            Expense expense = expenseRepository.findById(invoiceExpense.getExpenseId())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Expense not found with id: " + invoiceExpense.getExpenseId()
                ));

            // Domain rule: Only credit expenses can belong to an invoice
            if (expense.getPaymentMethod() != PaymentMethod.CREDIT) {
                throw new IllegalStateException(
                    "Only credit expenses can belong to an invoice. Expense id: " + expense.getId()
                );
            }

            // Mark expense as paid
            expense.setStatus(ExpenseStatus.PAID);
            expenseRepository.save(expense);
        }

        // Mark invoice as paid
        invoice.setStatus(InvoiceStatus.PAID);
        invoice = invoiceRepository.save(invoice);

        List<Long> expenseIds = invoiceExpenses.stream()
            .map(InvoiceExpense::getExpenseId)
            .collect(Collectors.toList());

        return invoiceMapper.toResponse(invoice, expenseIds);
    }
}

