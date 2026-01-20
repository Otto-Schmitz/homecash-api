package com.app.homecash.service;

import com.app.homecash.dto.request.LoginRequest;
import com.app.homecash.dto.request.RegisterRequest;
import com.app.homecash.dto.response.AuthResponse;

public interface AuthService {

    /**
     * Register a new user and return authentication token.
     * Validates email and CPF uniqueness.
     *
     * @param request registration data with password
     * @return AuthResponse DTO with JWT token and user data
     * @throws IllegalArgumentException if email or CPF already exists
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticate user and return JWT token.
     * Validates email and password.
     *
     * @param request login credentials (email and password)
     * @return AuthResponse DTO with JWT token and user data
     * @throws IllegalArgumentException if credentials are invalid
     */
    AuthResponse login(LoginRequest request);
}

