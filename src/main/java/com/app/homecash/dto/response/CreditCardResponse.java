package com.app.homecash.dto.response;

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
public class CreditCardResponse {

    private Long id;
    private Long userId;
    private String name;
    private String brand;
    private String lastDigits;
    private Long limitCents;
    private Integer closingDay;
    private Integer dueDay;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

