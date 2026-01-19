package com.app.homecash.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "credit_cards", indexes = {
    @Index(name = "idx_credit_card_user", columnList = "userId")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @NotBlank
    @Column(name = "last_digits", nullable = false)
    private String lastDigits;

    @NotNull
    @Positive
    @Column(name = "limit_cents", nullable = false)
    private Long limitCents;

    @NotNull
    @Column(name = "closing_day", nullable = false)
    private Integer closingDay;

    @NotNull
    @Column(name = "due_day", nullable = false)
    private Integer dueDay;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

