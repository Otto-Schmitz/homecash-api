package com.app.homecash.controller;

import com.app.homecash.dto.request.CreateHouseRequest;
import com.app.homecash.dto.request.InviteHouseRequest;
import com.app.homecash.dto.response.CreateHouseResponse;
import com.app.homecash.dto.response.HouseResponse;
import com.app.homecash.dto.response.InviteUserResponse;
import com.app.homecash.service.HouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/houses")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @PostMapping
    public ResponseEntity<CreateHouseResponse> create(
            @Valid @RequestBody CreateHouseRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        CreateHouseResponse response = houseService.create(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<HouseResponse>> getHouses(@RequestHeader("X-User-Id") Long userId) {
        List<HouseResponse> responses = houseService.getHousesByUser(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HouseResponse> getById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        HouseResponse response = houseService.getById(id, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<InviteUserResponse> updateInviteCode(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        HouseResponse houseResponse = houseService.updateInviteCode(id, userId);
        
        InviteUserResponse response = InviteUserResponse.builder()
            .houseId(houseResponse.getId())
            .houseName(houseResponse.getName())
            .inviteCode(houseResponse.getInviteCode())
            .updatedAt(houseResponse.getCreatedAt())
            .build();
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/join")
    public ResponseEntity<HouseResponse> joinHouse(
            @Valid @RequestBody InviteHouseRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        HouseResponse response = houseService.joinHouse(request.getInviteCode(), userId);
        return ResponseEntity.ok(response);
    }
}

