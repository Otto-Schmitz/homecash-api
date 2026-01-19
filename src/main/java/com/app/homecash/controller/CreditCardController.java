package com.app.homecash.controller;

import com.app.homecash.dto.request.CreateCreditCardRequest;
import com.app.homecash.dto.response.CreditCardResponse;
import com.app.homecash.service.CreditCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CreditCardController {

    private final CreditCardService creditCardService;

    @PostMapping
    public ResponseEntity<CreditCardResponse> create(
            @Valid @RequestBody CreateCreditCardRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        CreditCardResponse response = creditCardService.create(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CreditCardResponse>> getCards(@RequestHeader("X-User-Id") Long userId) {
        List<CreditCardResponse> responses = creditCardService.getByUser(userId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditCardResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody CreateCreditCardRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        CreditCardResponse response = creditCardService.update(id, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id,
            @RequestHeader("X-User-Id") Long userId) {
        creditCardService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}

