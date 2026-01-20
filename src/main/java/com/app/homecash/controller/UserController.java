package com.app.homecash.controller;

import com.app.homecash.dto.request.CreateUserRequest;
import com.app.homecash.dto.response.CreateUserResponse;
import com.app.homecash.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // User management endpoints (admin/internal use)
    // Auth endpoints are in AuthController
}

