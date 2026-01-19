package com.app.homecash.mapper;

import com.app.homecash.domain.Expense;
import com.app.homecash.dto.request.CreateExpenseRequest;
import com.app.homecash.dto.request.UpdateExpenseRequest;
import com.app.homecash.dto.response.ExpenseResponse;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    /**
     * Maps CreateExpenseRequest to Expense entity using builder.
     *
     * @param request DTO with expense data
     * @return Expense entity
     */
    public Expense toEntity(CreateExpenseRequest request) {
        if (request == null) {
            return null;
        }

        return Expense.builder()
            .title(request.getTitle())
            .category(request.getCategory())
            .amountCents(request.getAmountCents())
            .type(request.getType())
            .dueDate(request.getDueDate())
            .paymentMethod(request.getPaymentMethod())
            .creditCardId(request.getCreditCardId())
            .build();
    }

    /**
     * Maps Expense entity to ExpenseResponse using builder.
     *
     * @param expense Expense entity
     * @return ExpenseResponse DTO
     */
    public ExpenseResponse toResponse(Expense expense) {
        if (expense == null) {
            return null;
        }

        return ExpenseResponse.builder()
            .id(expense.getId())
            .houseId(expense.getHouseId())
            .title(expense.getTitle())
            .category(expense.getCategory())
            .amountCents(expense.getAmountCents())
            .type(expense.getType())
            .createdAt(expense.getCreatedAt())
            .dueDate(expense.getDueDate())
            .paymentMethod(expense.getPaymentMethod())
            .status(expense.getStatus())
            .createdBy(expense.getCreatedBy())
            .creditCardId(expense.getCreditCardId())
            .build();
    }
}

