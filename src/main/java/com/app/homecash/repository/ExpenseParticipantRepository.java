package com.app.homecash.repository;

import com.app.homecash.domain.ExpenseParticipant;
import com.app.homecash.domain.ParticipantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseParticipantRepository extends JpaRepository<ExpenseParticipant, Long> {

    List<ExpenseParticipant> findByExpenseId(Long expenseId);

    Optional<ExpenseParticipant> findByExpenseIdAndUserId(Long expenseId, Long userId);

    List<ExpenseParticipant> findByUserId(Long userId);

    List<ExpenseParticipant> findByExpenseIdAndStatus(Long expenseId, ParticipantStatus status);

    long countByExpenseId(Long expenseId);
}

