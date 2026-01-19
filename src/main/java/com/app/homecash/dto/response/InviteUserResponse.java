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
public class InviteUserResponse {

    private Long houseId;
    private String houseName;
    private String inviteCode;
    private LocalDateTime updatedAt;
}

