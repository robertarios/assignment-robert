package com.company.storemanager.service;

import com.company.storemanager.dto.AuthResponse;
import com.company.storemanager.dto.LoginRequest;
import com.company.storemanager.dto.RegisterRequest;
import com.company.storemanager.model.User;
import com.company.storemanager.repository.UserRepository;
import com.company.storemanager.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new AuthResponse("Username already exists");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new AuthResponse("Email already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setIsActive(true);

        userRepository.save(user);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, user.getUsername(), user.getFullName(), user.getEmail(), "Registration successful");
    }

    public AuthResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            final String token = jwtUtil.generateToken(userDetails);

            User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

            return new AuthResponse(token, user.getUsername(), user.getFullName(), user.getEmail(), "Login successful");

        } catch (Exception e) {
            return new AuthResponse("Invalid username or password");
        }
    }

    public boolean validateToken(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtUtil.validateToken(token, userDetails);
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            return jwtUtil.extractUsername(token);
        } catch (Exception e) {
            return null;
        }
    }
}