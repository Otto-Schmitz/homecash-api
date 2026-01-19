package com.app.homecash.mapper;

import com.app.homecash.domain.Invoice;
import com.app.homecash.dto.response.InvoiceResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceMapper {

    /**
     * Maps Invoice entity to InvoiceResponse using builder.
     *
     * @param invoice Invoice entity
     * @param expenseIds list of expense ids linked to the invoice
     * @return InvoiceResponse DTO
     */
    public InvoiceResponse toResponse(Invoice invoice, List<Long> expenseIds) {
        if (invoice == null) {
            return null;
        }

        return InvoiceResponse.builder()
            .id(invoice.getId())
            .creditCardId(invoice.getCreditCardId())
            .month(invoice.getMonth())
            .year(invoice.getYear())
            .totalCents(invoice.getTotalCents())
            .status(invoice.getStatus())
            .createdAt(invoice.getCreatedAt())
            .updatedAt(invoice.getUpdatedAt())
            .expenseIds(expenseIds)
            .build();
    }
}

