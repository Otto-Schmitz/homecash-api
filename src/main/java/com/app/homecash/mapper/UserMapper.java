package com.app.homecash.mapper;

import com.app.homecash.domain.User;
import com.app.homecash.dto.request.CreateUserRequest;
import com.app.homecash.dto.response.CreateUserResponse;
import com.app.homecash.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /**
     * Maps CreateUserRequest to User entity using builder.
     *
     * @param request DTO with user data
     * @return User entity
     */
    public User toEntity(CreateUserRequest request) {
        if (request == null) {
            return null;
        }

        return User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .cpf(request.getCpf())
            .phoneNumber(request.getPhoneNumber())
            .birthDate(request.getBirthDate())
            .build();
    }

    /**
     * Maps User entity to CreateUserResponse using builder.
     *
     * @param user User entity
     * @return CreateUserResponse DTO
     */
    public CreateUserResponse toCreateResponse(User user) {
        if (user == null) {
            return null;
        }

        return CreateUserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .cpf(user.getCpf())
            .phoneNumber(user.getPhoneNumber())
            .birthDate(user.getBirthDate())
            .active(user.getActive())
            .createdAt(user.getCreatedAt())
            .build();
    }

    /**
     * Maps User entity to UserResponse using builder.
     *
     * @param user User entity
     * @return UserResponse DTO
     */
    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .cpf(user.getCpf())
            .phoneNumber(user.getPhoneNumber())
            .birthDate(user.getBirthDate())
            .active(user.getActive())
            .createdAt(user.getCreatedAt())
            .build();
    }
}

