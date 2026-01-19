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
public class HouseResponse {

    private Long id;
    private String name;
    private String inviteCode;
    private LocalDateTime createdAt;
}

