package com.app.homecash.controller;

import com.app.homecash.dto.request.CreateExpenseRequest;
import com.app.homecash.dto.request.UpdateExpenseRequest;
import com.app.homecash.dto.response.ExpenseResponse;
import com.app.homecash.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/houses/{houseId}/expenses")
    public ResponseEntity<ExpenseResponse> create(
            @PathVariable("houseId") Long houseId,
            @Valid @RequestBody CreateExpenseRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        ExpenseResponse response = expenseService.create(houseId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/houses/{houseId}/expenses")
    public ResponseEntity<List<ExpenseResponse>> getByHouse(
            @PathVariable("houseId") Long houseId,
            @RequestHeader("X-User-Id") Long userId) {
        List<ExpenseResponse> responses = expenseService.getByHouse(houseId, userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/expenses/{id}")
    public ResponseEntity<ExpenseResponse> getById(
            @PathVariable("id") Long id,
            @RequestHeader("X-User-Id") Long userId) {
        ExpenseResponse response = expenseService.getById(id, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/expenses/{id}")
    public ResponseEntity<ExpenseResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateExpenseRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        ExpenseResponse response = expenseService.update(id, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id,
            @RequestHeader("X-User-Id") Long userId) {
        expenseService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/expenses/{id}/pay")
    public ResponseEntity<ExpenseResponse> markAsPaid(
            @PathVariable("id") Long id,
            @RequestHeader("X-User-Id") Long userId) {
        ExpenseResponse response = expenseService.markAsPaid(id, userId);
        return ResponseEntity.ok(response);
    }
}

