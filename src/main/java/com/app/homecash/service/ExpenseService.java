package com.app.homecash.service;

import com.app.homecash.dto.request.CreateExpenseRequest;
import com.app.homecash.dto.request.UpdateExpenseRequest;
import com.app.homecash.dto.response.ExpenseResponse;

import java.util.List;

public interface ExpenseService {

    /**
     * Create a new expense.
     * Validates that user belongs to house.
     * Amount must be > 0.
     * If payment method = CREDIT, must have credit card.
     *
     * @param houseId house id
     * @param request expense data
     * @param userId creator user id
     * @return ExpenseResponse DTO
     * @throws IllegalArgumentException if validation fails
     */
    ExpenseResponse create(Long houseId, CreateExpenseRequest request, Long userId);

    /**
     * Get all expenses for a house.
     * Validates that user belongs to house.
     * Always filters by house (multi-tenant).
     *
     * @param houseId house id
     * @param userId authenticated user id
     * @return List of ExpenseResponse DTOs
     */
    List<ExpenseResponse> getByHouse(Long houseId, Long userId);

    /**
     * Get expense by id.
     * Validates that user belongs to the expense's house.
     *
     * @param expenseId expense id
     * @param userId authenticated user id
     * @return ExpenseResponse DTO
     * @throws IllegalArgumentException if expense not found or user doesn't belong to house
     */
    ExpenseResponse getById(Long expenseId, Long userId);

    /**
     * Update an expense.
     * Validates that user belongs to house.
     * Amount must be > 0.
     * Cannot update if expense is already paid.
     *
     * @param expenseId expense id
     * @param request updated expense data
     * @param userId authenticated user id
     * @return ExpenseResponse DTO
     * @throws IllegalArgumentException if validation fails
     * @throws IllegalStateException if expense is already paid
     */
    ExpenseResponse update(Long expenseId, UpdateExpenseRequest request, Long userId);

    /**
     * Delete an expense.
     * Validates that user belongs to house.
     * Cannot delete if expense is already paid.
     *
     * @param expenseId expense id
     * @param userId authenticated user id
     * @throws IllegalArgumentException if expense not found or user doesn't belong to house
     * @throws IllegalStateException if expense is already paid
     */
    void delete(Long expenseId, Long userId);

    /**
     * Mark expense as paid.
     * Validates that user belongs to house.
     * Cannot be marked as paid without participants.
     *
     * @param expenseId expense id
     * @param userId authenticated user id
     * @return ExpenseResponse DTO
     * @throws IllegalArgumentException if expense not found or user doesn't belong to house
     * @throws IllegalStateException if expense has no participants
     */
    ExpenseResponse markAsPaid(Long expenseId, Long userId);
}

