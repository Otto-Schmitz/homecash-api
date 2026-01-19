package com.app.homecash.service.impl;

import com.app.homecash.domain.House;
import com.app.homecash.domain.HouseMember;
import com.app.homecash.domain.Role;
import com.app.homecash.dto.request.CreateHouseRequest;
import com.app.homecash.dto.response.CreateHouseResponse;
import com.app.homecash.dto.response.HouseResponse;
import com.app.homecash.mapper.HouseMapper;
import com.app.homecash.repository.HouseMemberRepository;
import com.app.homecash.repository.HouseRepository;
import com.app.homecash.repository.UserRepository;
import com.app.homecash.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final HouseMemberRepository houseMemberRepository;
    private final UserRepository userRepository;
    private final HouseMapper houseMapper;

    @Override
    @Transactional
    public CreateHouseResponse create(CreateHouseRequest houseRequest, Long userId) {
        // Validate user exists
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (!user.getActive()) {
            throw new IllegalStateException("Cannot create house for inactive user");
        }

        // Map DTO to entity
        House house = houseMapper.toEntity(houseRequest);

        // Generate unique invite code
        String inviteCode = generateUniqueInviteCode();
        house.setInviteCode(inviteCode);

        house = houseRepository.save(house);

        // Domain rule: Creator automatically becomes the owner
        // Domain rule: House must always have at least one member (the owner)
        HouseMember owner = HouseMember.builder()
            .userId(userId)
            .houseId(house.getId())
            .role(Role.OWNER)
            .build();

        houseMemberRepository.save(owner);

        // Map entity to response DTO
        return houseMapper.toCreateResponse(house);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HouseResponse> getHousesByUser(Long userId) {
        // Always filter by authenticated user (multi-tenant)
        var houseMembers = houseMemberRepository.findByUserId(userId);
        
        return houseMembers.stream()
            .map(hm -> houseRepository.findById(hm.getHouseId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(houseMapper::toHouseResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HouseResponse getById(Long houseId, Long userId) {
        // Validate that user belongs to the house
        if (!userBelongsToHouse(userId, houseId)) {
            throw new IllegalArgumentException(
                "User does not belong to house with id: " + houseId
            );
        }

        House house = houseRepository.findById(houseId)
            .orElseThrow(() -> new IllegalArgumentException("House not found with id: " + houseId));

        return houseMapper.toHouseResponse(house);
    }

    @Override
    @Transactional
    public HouseResponse updateInviteCode(Long houseId, Long userId) {
        // Validate user is owner
        validateUserPermission(userId, houseId, true);

        House house = houseRepository.findById(houseId)
            .orElseThrow(() -> new IllegalArgumentException("House not found with id: " + houseId));

        // Generate new unique invite code
        String newInviteCode = generateUniqueInviteCode();
        house.setInviteCode(newInviteCode);
        house = houseRepository.save(house);

        return houseMapper.toHouseResponse(house);
    }

    @Override
    @Transactional
    public HouseResponse joinHouse(String inviteCode, Long userId) {
        // Find house by invite code
        House house = houseRepository.findByInviteCode(inviteCode)
            .orElseThrow(() -> new IllegalArgumentException("Invalid invite code"));

        // Validate user exists and is active
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (!user.getActive()) {
            throw new IllegalStateException("Cannot join house with inactive user");
        }

        // Check if user is already a member
        Optional<HouseMember> existingMember = houseMemberRepository.findByUserIdAndHouseId(
            userId, 
            house.getId()
        );

        if (existingMember.isPresent()) {
            throw new IllegalArgumentException("User is already a member of this house");
        }

        // Domain rule: User becomes a member (not owner)
        // Domain rule: House must always have at least one member
        HouseMember member = HouseMember.builder()
            .userId(userId)
            .houseId(house.getId())
            .role(Role.MEMBER)
            .build();

        houseMemberRepository.save(member);

        return houseMapper.toHouseResponse(house);
    }

    @Override
    @Transactional
    public void addMember(Long houseId, Long memberUserId, Long ownerUserId) {
        // Validate owner permission
        validateUserPermission(ownerUserId, houseId, true);

        // Validate house exists
        House house = houseRepository.findById(houseId)
            .orElseThrow(() -> new IllegalArgumentException("House not found with id: " + houseId));

        // Validate member user exists and is active
        var memberUser = userRepository.findById(memberUserId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + memberUserId));

        if (!memberUser.getActive()) {
            throw new IllegalStateException("Cannot add inactive user as member");
        }

        // Check if user is already a member
        Optional<HouseMember> existingMember = houseMemberRepository.findByUserIdAndHouseId(
            memberUserId, 
            houseId
        );

        if (existingMember.isPresent()) {
            throw new IllegalArgumentException("User is already a member of this house");
        }

        // Domain rule: House must always have at least one member
        HouseMember member = HouseMember.builder()
            .userId(memberUserId)
            .houseId(houseId)
            .role(Role.MEMBER)
            .build();

        houseMemberRepository.save(member);
    }

    @Override
    @Transactional
    public void removeMember(Long houseId, Long memberUserId, Long ownerUserId) {
        // Validate owner permission
        validateUserPermission(ownerUserId, houseId, true);

        // Validate house exists
        House house = houseRepository.findById(houseId)
            .orElseThrow(() -> new IllegalArgumentException("House not found with id: " + houseId));

        // Get the member to remove
        HouseMember member = houseMemberRepository.findByUserIdAndHouseId(memberUserId, houseId)
            .orElseThrow(() -> new IllegalArgumentException("User is not a member of this house"));

        // Domain rule: Owner cannot remove himself if he is the only owner
        if (member.getUserId().equals(ownerUserId) && member.getRole() == Role.OWNER) {
            long ownerCount = houseMemberRepository.countByHouseIdAndRole(houseId, Role.OWNER);
            if (ownerCount == 1) {
                throw new IllegalStateException(
                    "Owner cannot remove himself if he is the only owner"
                );
            }
        }

        // Domain rule: House must always have at least one member
        long totalMembers = houseMemberRepository.countByHouseId(houseId);
        if (totalMembers <= 1) {
            throw new IllegalStateException(
                "Cannot remove member: house must always have at least one member"
            );
        }

        // Domain rule: House must always have one owner
        if (member.getRole() == Role.OWNER) {
            long ownerCount = houseMemberRepository.countByHouseIdAndRole(houseId, Role.OWNER);
            if (ownerCount <= 1) {
                throw new IllegalStateException(
                    "Cannot remove member: house must always have at least one owner"
                );
            }
        }

        houseMemberRepository.delete(member);
    }

    @Override
    @Transactional
    public void deleteHouse(Long houseId, Long userId) {
        // Validate owner permission
        validateUserPermission(userId, houseId, true);

        // Validate house exists
        House house = houseRepository.findById(houseId)
            .orElseThrow(() -> new IllegalArgumentException("House not found with id: " + houseId));

        // Domain rule: House must always have at least one member
        // This check ensures we're not deleting a house that violates domain rules
        long memberCount = houseMemberRepository.countByHouseId(houseId);
        if (memberCount == 0) {
            throw new IllegalStateException("House has no members");
        }

        // Delete all members first
        List<HouseMember> members = houseMemberRepository.findByHouseId(houseId);
        houseMemberRepository.deleteAll(members);

        // Delete house
        houseRepository.delete(house);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userBelongsToHouse(Long userId, Long houseId) {
        return houseMemberRepository.findByUserIdAndHouseId(userId, houseId).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserOwner(Long userId, Long houseId) {
        Optional<HouseMember> member = houseMemberRepository.findByUserIdAndHouseId(userId, houseId);
        return member.isPresent() && member.get().getRole() == Role.OWNER;
    }

    @Override
    @Transactional(readOnly = true)
    public void validateUserPermission(Long userId, Long houseId, boolean requireOwner) {
        // Always validate that user belongs to house
        if (!userBelongsToHouse(userId, houseId)) {
            throw new IllegalStateException(
                "User does not belong to house with id: " + houseId
            );
        }

        // If owner permission is required, validate it
        if (requireOwner && !isUserOwner(userId, houseId)) {
            throw new IllegalStateException(
                "User does not have owner permission for house with id: " + houseId
            );
        }
    }

    /**
     * Generate a unique invite code for a house.
     * Uses UUID and ensures uniqueness in the database.
     */
    private String generateUniqueInviteCode() {
        String inviteCode;
        boolean isUnique = false;
        int attempts = 0;
        final int MAX_ATTEMPTS = 10;

        do {
            // Generate invite code from UUID (first 8 characters, uppercase)
            inviteCode = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
            isUnique = houseRepository.findByInviteCode(inviteCode).isEmpty();
            attempts++;
        } while (!isUnique && attempts < MAX_ATTEMPTS);

        if (!isUnique) {
            throw new IllegalStateException("Failed to generate unique invite code after " + MAX_ATTEMPTS + " attempts");
        }

        return inviteCode;
    }
}

