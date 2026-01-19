package com.app.homecash.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "expense_participants", indexes = {
    @Index(name = "idx_expense_participant_expense", columnList = "expenseId"),
    @Index(name = "idx_expense_participant_user", columnList = "userId"),
    @Index(name = "idx_expense_participant_unique", columnList = "expenseId,userId", unique = true)
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "expense_id", nullable = false)
    private Long expenseId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Positive
    @Column(name = "amount_cents", nullable = false)
    private Long amountCents;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ParticipantStatus status = ParticipantStatus.OWES;

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = ParticipantStatus.OWES;
        }
    }
}

