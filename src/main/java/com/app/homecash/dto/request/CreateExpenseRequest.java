package com.app.homecash.dto.request;

import com.app.homecash.domain.ExpenseType;
import com.app.homecash.domain.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExpenseRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Long amountCents;

    @NotNull(message = "Type is required")
    private ExpenseType type;

    private LocalDateTime dueDate;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private Long creditCardId;
}

