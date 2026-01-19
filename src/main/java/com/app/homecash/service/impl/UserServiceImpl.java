package com.app.homecash.service.impl;

import com.app.homecash.domain.Role;
import com.app.homecash.domain.User;
import com.app.homecash.dto.request.CreateUserRequest;
import com.app.homecash.dto.response.CreateUserResponse;
import com.app.homecash.dto.response.UserResponse;
import com.app.homecash.mapper.UserMapper;
import com.app.homecash.repository.HouseMemberRepository;
import com.app.homecash.repository.UserRepository;
import com.app.homecash.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final HouseMemberRepository houseMemberRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public CreateUserResponse register(CreateUserRequest userRequest) {
        // Domain rule: Email must be unique
        if (!isEmailAvailable(userRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userRequest.getEmail());
        }

        // Domain rule: CPF must be unique
        if (!isCpfAvailable(userRequest.getCpf())) {
            throw new IllegalArgumentException("CPF already exists: " + userRequest.getCpf());
        }

        // Map DTO to entity
        User user = userMapper.toEntity(userRequest);
        user = userRepository.save(user);

        // Map entity to response DTO
        return userMapper.toCreateResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(userMapper::toUserResponse).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findByCpf(String cpf) {
        Optional<User> user = userRepository.findByCpf(cpf);
        return user.map(userMapper::toUserResponse).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findActiveByEmail(String email) {
        Optional<User> user = userRepository.findByEmailAndActiveTrue(email);
        return user.map(userMapper::toUserResponse).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        // Email must be unique (domain rule)
        return userRepository.findByEmail(email).isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCpfAvailable(String cpf) {
        // CPF must be unique (domain rule)
        return userRepository.findByCpf(cpf).isEmpty();
    }

    @Override
    @Transactional
    public void disableUser(Long userId) {
        // Domain rule: Cannot be removed if it is the only owner of a house
        if (!canUserBeRemoved(userId)) {
            throw new IllegalStateException(
                "User cannot be removed because they are the only owner of at least one house"
            );
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Soft delete
        user.setActive(false);
        user.setDisabledAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canUserBeRemoved(Long userId) {
        // Domain rule: Cannot be removed if it is the only owner of a house
        // Check if user is the only owner in any house
        var userHouses = houseMemberRepository.findByUserId(userId);
        
        for (var houseMember : userHouses) {
            if (houseMember.getRole() == Role.OWNER) {
                long ownerCount = houseMemberRepository.countByHouseIdAndRole(
                    houseMember.getHouseId(), 
                    Role.OWNER
                );
                
                // If user is the only owner of this house, they cannot be removed
                if (ownerCount == 1) {
                    return false;
                }
            }
        }
        
        return true;
    }
}

