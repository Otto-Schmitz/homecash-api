package com.app.homecash.dto.response;

import com.app.homecash.domain.ExpenseStatus;
import com.app.homecash.domain.ExpenseType;
import com.app.homecash.domain.PaymentMethod;
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
public class ExpenseResponse {

    private Long id;
    private Long houseId;
    private String title;
    private String category;
    private Long amountCents;
    private ExpenseType type;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private PaymentMethod paymentMethod;
    private ExpenseStatus status;
    private Long createdBy;
    private Long creditCardId;
}

