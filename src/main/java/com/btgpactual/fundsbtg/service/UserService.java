package com.btgpactual.fundsbtg.service;

import com.btgpactual.fundsbtg.dto.request.LoginRequest;
import com.btgpactual.fundsbtg.dto.request.UserRegistrationRequest;
import com.btgpactual.fundsbtg.dto.response.AuthResponse;
import com.btgpactual.fundsbtg.dto.response.UserResponse;
import reactor.core.publisher.Mono;

public interface UserService {
    
    Mono<UserResponse> registerUser(UserRegistrationRequest request);
    
    Mono<AuthResponse> login(LoginRequest request);
    
    Mono<UserResponse> getUserById(String userId);
    
    Mono<UserResponse> getUserByEmail(String email);
}
