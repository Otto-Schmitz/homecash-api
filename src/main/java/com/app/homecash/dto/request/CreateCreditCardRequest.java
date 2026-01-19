package com.app.homecash.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCreditCardRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Last digits are required")
    private String lastDigits;

    @NotNull(message = "Limit is required")
    @Positive(message = "Limit must be greater than 0")
    private Long limitCents;

    @NotNull(message = "Closing day is required")
    @Min(value = 1, message = "Closing day must be between 1 and 31")
    @Max(value = 31, message = "Closing day must be between 1 and 31")
    private Integer closingDay;

    @NotNull(message = "Due day is required")
    @Min(value = 1, message = "Due day must be between 1 and 31")
    @Max(value = 31, message = "Due day must be between 1 and 31")
    private Integer dueDay;
}

