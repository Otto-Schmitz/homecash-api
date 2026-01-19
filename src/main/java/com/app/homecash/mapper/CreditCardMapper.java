package com.app.homecash.mapper;

import com.app.homecash.domain.CreditCard;
import com.app.homecash.dto.request.CreateCreditCardRequest;
import com.app.homecash.dto.response.CreditCardResponse;
import org.springframework.stereotype.Component;

@Component
public class CreditCardMapper {

    /**
     * Maps CreateCreditCardRequest to CreditCard entity using builder.
     *
     * @param request DTO with credit card data
     * @return CreditCard entity
     */
    public CreditCard toEntity(CreateCreditCardRequest request) {
        if (request == null) {
            return null;
        }

        return CreditCard.builder()
            .name(request.getName())
            .brand(request.getBrand())
            .lastDigits(request.getLastDigits())
            .limitCents(request.getLimitCents())
            .closingDay(request.getClosingDay())
            .dueDay(request.getDueDay())
            .build();
    }

    /**
     * Maps CreditCard entity to CreditCardResponse using builder.
     *
     * @param creditCard CreditCard entity
     * @return CreditCardResponse DTO
     */
    public CreditCardResponse toResponse(CreditCard creditCard) {
        if (creditCard == null) {
            return null;
        }

        return CreditCardResponse.builder()
            .id(creditCard.getId())
            .userId(creditCard.getUserId())
            .name(creditCard.getName())
            .brand(creditCard.getBrand())
            .lastDigits(creditCard.getLastDigits())
            .limitCents(creditCard.getLimitCents())
            .closingDay(creditCard.getClosingDay())
            .dueDay(creditCard.getDueDay())
            .createdAt(creditCard.getCreatedAt())
            .updatedAt(creditCard.getUpdatedAt())
            .build();
    }
}

