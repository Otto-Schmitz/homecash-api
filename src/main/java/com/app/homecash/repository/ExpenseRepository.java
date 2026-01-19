package com.app.homecash.repository;

import com.app.homecash.domain.Expense;
import com.app.homecash.domain.ExpenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByHouseId(Long houseId);

    List<Expense> findByHouseIdAndStatus(Long houseId, ExpenseStatus status);

    List<Expense> findByCreatedBy(Long userId);
}

