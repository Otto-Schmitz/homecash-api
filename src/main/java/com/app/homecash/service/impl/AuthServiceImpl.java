package com.app.homecash.service.impl;

import com.app.homecash.domain.User;
import com.app.homecash.dto.request.CreateUserRequest;
import com.app.homecash.dto.request.LoginRequest;
import com.app.homecash.dto.request.RegisterRequest;
import com.app.homecash.dto.response.AuthResponse;
import com.app.homecash.mapper.UserMapper;
import com.app.homecash.repository.UserRepository;
import com.app.homecash.service.AuthService;
import com.app.homecash.service.UserService;
import com.app.homecash.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validate email and CPF availability using UserService
        if (!userService.isEmailAvailable(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        if (!userService.isCpfAvailable(request.getCpf())) {
            throw new IllegalArgumentException("CPF already exists: " + request.getCpf());
        }

        // Create user without password using existing UserService
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
            .name(request.getName())
            .email(request.getEmail())
            .cpf(request.getCpf())
            .phoneNumber(request.getPhoneNumber())
            .birthDate(request.getBirthDate())
            .build();

        var userResponse = userService.register(createUserRequest);

        // Get the created user to add password
        User user = userRepository.findById(userResponse.getId())
            .orElseThrow(() -> new IllegalArgumentException("Failed to create user"));

        // Encrypt and store password
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encryptedPassword);
        user = userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getId().toString(), user.getEmail());

        return AuthResponse.builder()
            .token(token)
            .userId(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // Find active user by email
        var userResponse = userService.findActiveByEmail(request.getEmail());
        if (userResponse == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        User user = userRepository.findById(userResponse.getId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!user.getActive()) {
            throw new IllegalArgumentException("User account is inactive");
        }

        // Validate password (Note: password validation needs to be implemented)
        // For now, we'll assume password validation is done
        // In a real scenario, User entity would have a password field that we'd compare
        if (!validatePassword(request.getPassword(), user)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getId().toString(), user.getEmail());

        return AuthResponse.builder()
            .token(token)
            .userId(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .build();
    }

    /**
     * Validate user password.
     * Compares the raw password with the encrypted password stored in User entity.
     */
    private boolean validatePassword(String rawPassword, User user) {
        if (user.getPassword() == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}

