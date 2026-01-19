package com.app.homecash.controller;

import com.app.homecash.dto.response.InvoiceResponse;
import com.app.homecash.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/cards/{id}/invoices")
    public ResponseEntity<List<InvoiceResponse>> getByCreditCard(
            @PathVariable("id") Long id,
            @RequestHeader("X-User-Id") Long userId) {
        List<InvoiceResponse> responses = invoiceService.getByCreditCard(id, userId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/invoices/{id}/pay")
    public ResponseEntity<InvoiceResponse> markAsPaid(
            @PathVariable("id") Long id,
            @RequestHeader("X-User-Id") Long userId) {
        InvoiceResponse response = invoiceService.markAsPaid(id, userId);
        return ResponseEntity.ok(response);
    }
}

