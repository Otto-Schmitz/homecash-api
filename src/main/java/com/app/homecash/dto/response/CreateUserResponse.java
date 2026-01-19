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
public class CreateUserResponse {

    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String phoneNumber;
    private LocalDateTime birthDate;
    private Boolean active;
    private LocalDateTime createdAt;
}

