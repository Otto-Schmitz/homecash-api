package com.app.homecash.service;

import com.app.homecash.dto.request.CreateCreditCardRequest;
import com.app.homecash.dto.response.CreditCardResponse;

import java.util.List;

public interface CreditCardService {

    /**
     * Create a new credit card.
     * Only owner can create credit cards.
     *
     * @param request credit card data
     * @param userId user id (must be owner)
     * @return CreditCardResponse DTO
     * @throws IllegalStateException if user is not an owner
     */
    CreditCardResponse create(CreateCreditCardRequest request, Long userId);

    /**
     * Get all credit cards for a user.
     * Always filters by authenticated user (multi-tenant).
     *
     * @param userId authenticated user id
     * @return List of CreditCardResponse DTOs
     */
    List<CreditCardResponse> getByUser(Long userId);

    /**
     * Get credit card by id.
     * Validates that user owns the credit card.
     *
     * @param cardId credit card id
     * @param userId authenticated user id
     * @return CreditCardResponse DTO
     * @throws IllegalArgumentException if credit card not found or user doesn't own it
     */
    CreditCardResponse getById(Long cardId, Long userId);

    /**
     * Update a credit card.
     * Only owner can update.
     * Validates that user owns the credit card.
     *
     * @param cardId credit card id
     * @param request updated credit card data
     * @param userId authenticated user id
     * @return CreditCardResponse DTO
     * @throws IllegalStateException if user is not an owner
     * @throws IllegalArgumentException if credit card not found or user doesn't own it
     */
    CreditCardResponse update(Long cardId, CreateCreditCardRequest request, Long userId);

    /**
     * Delete a credit card.
     * Only owner can delete.
     * Validates that user owns the credit card.
     *
     * @param cardId credit card id
     * @param userId authenticated user id
     * @throws IllegalStateException if user is not an owner
     * @throws IllegalArgumentException if credit card not found or user doesn't own it
     */
    void delete(Long cardId, Long userId);
}

