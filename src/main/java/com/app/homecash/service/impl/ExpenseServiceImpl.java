package com.app.homecash.service.impl;

import com.app.homecash.domain.Expense;
import com.app.homecash.domain.ExpenseParticipant;
import com.app.homecash.domain.ExpenseStatus;
import com.app.homecash.domain.ParticipantStatus;
import com.app.homecash.domain.PaymentMethod;
import com.app.homecash.dto.request.CreateExpenseRequest;
import com.app.homecash.dto.request.UpdateExpenseRequest;
import com.app.homecash.dto.response.ExpenseResponse;
import com.app.homecash.mapper.ExpenseMapper;
import com.app.homecash.repository.ExpenseParticipantRepository;
import com.app.homecash.repository.ExpenseRepository;
import com.app.homecash.repository.HouseRepository;
import com.app.homecash.repository.UserRepository;
import com.app.homecash.service.ExpenseService;
import com.app.homecash.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseParticipantRepository expenseParticipantRepository;
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;
    private final HouseService houseService;
    private final ExpenseMapper expenseMapper;

    @Override
    @Transactional
    public ExpenseResponse create(Long houseId, CreateExpenseRequest request, Long userId) {
        // Validate user belongs to house
        houseService.validateUserPermission(userId, houseId, false);

        // Domain rule: Amount > 0
        if (request.getAmountCents() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        // Domain rule: If payment method = CREDIT, must have credit card
        if (request.getPaymentMethod() == PaymentMethod.CREDIT && request.getCreditCardId() == null) {
            throw new IllegalArgumentException("Credit card is required when payment method is CREDIT");
        }

        // Validate house exists
        houseRepository.findById(houseId)
            .orElseThrow(() -> new IllegalArgumentException("House not found with id: " + houseId));

        // Map DTO to entity
        Expense expense = expenseMapper.toEntity(request);
        expense.setHouseId(houseId);
        expense.setCreatedBy(userId);

        expense = expenseRepository.save(expense);

        return expenseMapper.toResponse(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getByHouse(Long houseId, Long userId) {
        // Validate user belongs to house (multi-tenant)
        houseService.validateUserPermission(userId, houseId, false);

        List<Expense> expenses = expenseRepository.findByHouseId(houseId);
        return expenses.stream()
            .map(expenseMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponse getById(Long expenseId, Long userId) {
        Expense expense = expenseRepository.findById(expenseId)
            .orElseThrow(() -> new IllegalArgumentException("Expense not found with id: " + expenseId));

        // Validate user belongs to the expense's house
        houseService.validateUserPermission(userId, expense.getHouseId(), false);

        return expenseMapper.toResponse(expense);
    }

    @Override
    @Transactional
    public ExpenseResponse update(Long expenseId, UpdateExpenseRequest request, Long userId) {
        Expense expense = expenseRepository.findById(expenseId)
            .orElseThrow(() -> new IllegalArgumentException("Expense not found with id: " + expenseId));

        // Validate user belongs to house
        houseService.validateUserPermission(userId, expense.getHouseId(), false);

        // Domain rule: Cannot update if expense is already paid
        if (expense.getStatus() == ExpenseStatus.PAID) {
            throw new IllegalStateException("Cannot update expense that is already paid");
        }

        // Domain rule: Amount > 0
        if (request.getAmountCents() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        // Domain rule: If payment method = CREDIT, must have credit card
        if (request.getPaymentMethod() == PaymentMethod.CREDIT && request.getCreditCardId() == null) {
            throw new IllegalArgumentException("Credit card is required when payment method is CREDIT");
        }

        // Update expense fields
        expense.setTitle(request.getTitle());
        expense.setCategory(request.getCategory());
        expense.setAmountCents(request.getAmountCents());
        expense.setType(request.getType());
        expense.setDueDate(request.getDueDate());
        expense.setPaymentMethod(request.getPaymentMethod());
        expense.setCreditCardId(request.getCreditCardId());

        expense = expenseRepository.save(expense);

        return expenseMapper.toResponse(expense);
    }

    @Override
    @Transactional
    public void delete(Long expenseId, Long userId) {
        Expense expense = expenseRepository.findById(expenseId)
            .orElseThrow(() -> new IllegalArgumentException("Expense not found with id: " + expenseId));

        // Validate user belongs to house
        houseService.validateUserPermission(userId, expense.getHouseId(), false);

        // Domain rule: Cannot delete if expense is already paid
        if (expense.getStatus() == ExpenseStatus.PAID) {
            throw new IllegalStateException("Cannot delete expense that is already paid");
        }

        // Delete participants first
        List<ExpenseParticipant> participants = expenseParticipantRepository.findByExpenseId(expenseId);
        expenseParticipantRepository.deleteAll(participants);

        // Delete expense
        expenseRepository.delete(expense);
    }

    @Override
    @Transactional
    public ExpenseResponse markAsPaid(Long expenseId, Long userId) {
        Expense expense = expenseRepository.findById(expenseId)
            .orElseThrow(() -> new IllegalArgumentException("Expense not found with id: " + expenseId));

        // Validate user belongs to house
        houseService.validateUserPermission(userId, expense.getHouseId(), false);

        // Domain rule: Cannot be marked as paid without participants
        List<ExpenseParticipant> participants = expenseParticipantRepository.findByExpenseId(expenseId);
        if (participants.isEmpty()) {
            throw new IllegalStateException("Cannot mark expense as paid without participants");
        }

        // Domain rule: Sum of participants = expense total
        long totalParticipants = participants.stream()
            .mapToLong(ExpenseParticipant::getAmountCents)
            .sum();

        if (totalParticipants != expense.getAmountCents()) {
            throw new IllegalStateException(
                "Sum of participants (" + totalParticipants + 
                ") must equal expense total (" + expense.getAmountCents() + ")"
            );
        }

        // Mark expense as paid
        expense.setStatus(ExpenseStatus.PAID);
        expense = expenseRepository.save(expense);

        return expenseMapper.toResponse(expense);
    }
}

