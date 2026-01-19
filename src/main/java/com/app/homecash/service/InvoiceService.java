package com.app.homecash.service;

import com.app.homecash.dto.response.InvoiceResponse;

import java.util.List;

public interface InvoiceService {

    /**
     * Get all invoices for a credit card.
     * Validates that user owns the credit card.
     * Always filters by authenticated user (multi-tenant).
     *
     * @param cardId credit card id
     * @param userId authenticated user id
     * @return List of InvoiceResponse DTOs
     * @throws IllegalArgumentException if credit card not found or user doesn't own it
     */
    List<InvoiceResponse> getByCreditCard(Long cardId, Long userId);

    /**
     * Get invoice by id.
     * Validates that user owns the credit card associated with the invoice.
     *
     * @param invoiceId invoice id
     * @param userId authenticated user id
     * @return InvoiceResponse DTO
     * @throws IllegalArgumentException if invoice not found or user doesn't own the credit card
     */
    InvoiceResponse getById(Long invoiceId, Long userId);

    /**
     * Mark invoice as paid.
     * Validates that user owns the credit card.
     * Domain rule: Paying invoice marks all linked expenses as paid.
     *
     * @param invoiceId invoice id
     * @param userId authenticated user id
     * @return InvoiceResponse DTO
     * @throws IllegalArgumentException if invoice not found or user doesn't own the credit card
     * @throws IllegalStateException if invoice cannot be paid
     */
    InvoiceResponse markAsPaid(Long invoiceId, Long userId);
}

