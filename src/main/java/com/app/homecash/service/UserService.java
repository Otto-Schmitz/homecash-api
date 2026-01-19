package com.app.homecash.service;

import com.app.homecash.dto.request.CreateUserRequest;
import com.app.homecash.dto.response.CreateUserResponse;
import com.app.homecash.dto.response.UserResponse;

public interface UserService {

    /**
     * Register a new user.
     * Validates email and CPF uniqueness.
     *
     * @param userRequest DTO with user data (name, email, cpf, phoneNumber, birthDate)
     * @return CreateUserResponse DTO with created user data
     * @throws IllegalArgumentException if email or CPF already exists
     */
    CreateUserResponse register(CreateUserRequest userRequest);

    /**
     * Find user by email.
     *
     * @param email user email
     * @return UserResponse DTO or null if not found
     */
    UserResponse findByEmail(String email);

    /**
     * Find user by CPF.
     *
     * @param cpf user CPF
     * @return UserResponse DTO or null if not found
     */
    UserResponse findByCpf(String cpf);

    /**
     * Find active user by email.
     *
     * @param email user email
     * @return UserResponse DTO or null if not found or inactive
     */
    UserResponse findActiveByEmail(String email);

    /**
     * Validate if email is available.
     *
     * @param email email to validate
     * @return true if email is available, false otherwise
     */
    boolean isEmailAvailable(String email);

    /**
     * Validate if CPF is available.
     *
     * @param cpf CPF to validate
     * @return true if CPF is available, false otherwise
     */
    boolean isCpfAvailable(String cpf);

    /**
     * Soft delete user.
     * Cannot remove if user is the only owner of a house.
     *
     * @param userId user id to disable
     * @throws IllegalStateException if user is the only owner of a house
     */
    void disableUser(Long userId);

    /**
     * Check if user can be removed.
     * A user cannot be removed if it is the only owner of a house.
     *
     * @param userId user id to check
     * @return true if user can be removed, false otherwise
     */
    boolean canUserBeRemoved(Long userId);
}

