package com.app.homecash.service;

import com.app.homecash.dto.request.CreateHouseRequest;
import com.app.homecash.dto.response.CreateHouseResponse;
import com.app.homecash.dto.response.HouseResponse;

import java.util.List;

public interface HouseService {

    /**
     * Create a new house.
     * The creator automatically becomes the owner.
     * House must always have at least one member (the owner).
     *
     * @param houseRequest DTO with house data (name)
     * @param userId creator user id (becomes owner)
     * @return CreateHouseResponse DTO with created house data (including inviteCode)
     */
    CreateHouseResponse create(CreateHouseRequest houseRequest, Long userId);

    /**
     * Get all houses where the user is a member.
     * Always filters by authenticated user (multi-tenant).
     *
     * @param userId authenticated user id
     * @return List of HouseResponse DTOs
     */
    List<HouseResponse> getHousesByUser(Long userId);

    /**
     * Get house by id.
     * Validates that user belongs to the house.
     *
     * @param houseId house id
     * @param userId authenticated user id
     * @return HouseResponse DTO
     * @throws IllegalArgumentException if house not found or user doesn't belong to house
     */
    HouseResponse getById(Long houseId, Long userId);

    /**
     * Generate or update invite code for a house.
     * Only owner can perform this action.
     *
     * @param houseId house id
     * @param userId authenticated user id
     * @return HouseResponse DTO with updated inviteCode
     * @throws IllegalStateException if user is not the owner
     * @throws IllegalArgumentException if house not found
     */
    HouseResponse updateInviteCode(Long houseId, Long userId);

    /**
     * Join a house using invite code.
     * User becomes a member (not owner).
     * House must always have at least one member.
     *
     * @param inviteCode house invite code
     * @param userId user id who wants to join
     * @return HouseResponse DTO
     * @throws IllegalArgumentException if invite code is invalid or user already a member
     */
    HouseResponse joinHouse(String inviteCode, Long userId);

    /**
     * Add a member to a house.
     * Only owner can add members.
     *
     * @param houseId house id
     * @param memberUserId user id to add as member
     * @param ownerUserId authenticated owner user id
     * @throws IllegalStateException if user is not the owner
     * @throws IllegalArgumentException if house or user not found, or user already a member
     */
    void addMember(Long houseId, Long memberUserId, Long ownerUserId);

    /**
     * Remove a member from a house.
     * Only owner can remove members.
     * Owner cannot remove himself if he is the only owner.
     * House must always have at least one member.
     *
     * @param houseId house id
     * @param memberUserId user id to remove
     * @param ownerUserId authenticated owner user id
     * @throws IllegalStateException if user is not the owner, or trying to remove the only owner
     * @throws IllegalArgumentException if house or user not found, or user is not a member
     */
    void removeMember(Long houseId, Long memberUserId, Long ownerUserId);

    /**
     * Delete a house.
     * Only owner can delete.
     * House must always have at least one member.
     *
     * @param houseId house id
     * @param userId authenticated owner user id
     * @throws IllegalStateException if user is not the owner
     * @throws IllegalArgumentException if house not found
     */
    void deleteHouse(Long houseId, Long userId);

    /**
     * Check if user belongs to house.
     *
     * @param userId user id
     * @param houseId house id
     * @return true if user is a member of the house, false otherwise
     */
    boolean userBelongsToHouse(Long userId, Long houseId);

    /**
     * Check if user is owner of house.
     *
     * @param userId user id
     * @param houseId house id
     * @return true if user is owner of the house, false otherwise
     */
    boolean isUserOwner(Long userId, Long houseId);

    /**
     * Validate that user has permission to perform action.
     * Validates both ownership and membership.
     *
     * @param userId user id
     * @param houseId house id
     * @param requireOwner true if action requires owner role, false if member is enough
     * @throws IllegalStateException if user doesn't have required permission
     */
    void validateUserPermission(Long userId, Long houseId, boolean requireOwner);
}

