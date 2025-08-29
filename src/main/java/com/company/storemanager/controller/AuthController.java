package com.company.storemanager.controller;

import com.company.storemanager.dto.AuthResponse;
import com.company.storemanager.dto.LoginRequest;
import com.company.storemanager.dto.RegisterRequest;
import com.company.storemanager.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authenticationService.register(registerRequest);
        
        if (response.getMessage().equals("Registration successful")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = authenticationService.login(loginRequest);
        
        if (response.getMessage().equals("Login successful")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout() {
        // Simple logout endpoint - client side should remove the token
        return ResponseEntity.ok(new AuthResponse("Logout successful. Please remove the token from client side."));
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return ResponseEntity.ok(false);
        }
        
        boolean isValid = authenticationService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }
}