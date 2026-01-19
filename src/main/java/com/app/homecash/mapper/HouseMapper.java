package com.app.homecash.mapper;

import com.app.homecash.domain.House;
import com.app.homecash.dto.request.CreateHouseRequest;
import com.app.homecash.dto.response.CreateHouseResponse;
import com.app.homecash.dto.response.HouseResponse;
import org.springframework.stereotype.Component;

@Component
public class HouseMapper {

    /**
     * Maps CreateHouseRequest to House entity using builder.
     *
     * @param request DTO with house data
     * @return House entity
     */
    public House toEntity(CreateHouseRequest request) {
        if (request == null) {
            return null;
        }

        return House.builder()
            .name(request.getName())
            .build();
    }

    /**
     * Maps House entity to CreateHouseResponse using builder.
     *
     * @param house House entity
     * @return CreateHouseResponse DTO
     */
    public CreateHouseResponse toCreateResponse(House house) {
        if (house == null) {
            return null;
        }

        return CreateHouseResponse.builder()
            .id(house.getId())
            .name(house.getName())
            .inviteCode(house.getInviteCode())
            .createdAt(house.getCreatedAt())
            .build();
    }

    /**
     * Maps House entity to HouseResponse using builder.
     *
     * @param house House entity
     * @return HouseResponse DTO
     */
    public HouseResponse toHouseResponse(House house) {
        if (house == null) {
            return null;
        }

        return HouseResponse.builder()
            .id(house.getId())
            .name(house.getName())
            .inviteCode(house.getInviteCode())
            .createdAt(house.getCreatedAt())
            .build();
    }
}

