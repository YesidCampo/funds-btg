package com.btgpactual.fundsbtg.service;

import com.btgpactual.fundsbtg.common.constants.AppConstants;
import com.btgpactual.fundsbtg.common.constants.NotificationType;
import com.btgpactual.fundsbtg.dto.request.LoginRequest;
import com.btgpactual.fundsbtg.dto.request.UserRegistrationRequest;
import com.btgpactual.fundsbtg.dto.response.AuthResponse;
import com.btgpactual.fundsbtg.dto.response.UserResponse;
import com.btgpactual.fundsbtg.exception.DuplicateResourceException;
import com.btgpactual.fundsbtg.exception.InvalidCredentialsException;
import com.btgpactual.fundsbtg.model.User;
import com.btgpactual.fundsbtg.repository.UserRepository;
import com.btgpactual.fundsbtg.security.JwtUtil;
import com.btgpactual.fundsbtg.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para UserServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtUtil jwtUtil;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    private UserRegistrationRequest registrationRequest;
    private User user;
    private LoginRequest loginRequest;
    
    @BeforeEach
    void setUp() {
        registrationRequest = UserRegistrationRequest.builder()
                .userName("Juan Pérez")
                .email("juan.perez@example.com")
                .nit("1234567890")
                .password("password123")
                .preferredNotification(NotificationType.EMAIL)
                .build();
        
        user = User.builder()
                .id("1")
                .userName("Juan Pérez")
                .email("juan.perez@example.com")
                .nit("1234567890")
                .password("encodedPassword")
                .balance(AppConstants.INITIAL_USER_BALANCE)
                .role(AppConstants.ROLE_USER)
                .preferredNotification(NotificationType.EMAIL)
                .active(true)
                .build();
        
        loginRequest = LoginRequest.builder()
                .email("juan.perez@example.com")
                .password("password123")
                .build();
    }
    
    @Test
    void registerUser_Success() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(Mono.just(false));
        when(userRepository.existsByNit(anyString())).thenReturn(Mono.just(false));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));
        
        // When
        Mono<UserResponse> result = userService.registerUser(registrationRequest);
        
        // Then
        StepVerifier.create(result)
                .expectNextMatches(userResponse -> 
                        userResponse.getEmail().equals("juan.perez@example.com") &&
                        userResponse.getUserName().equals("Juan Pérez"))
                .verifyComplete();
        
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).existsByNit(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void registerUser_DuplicateEmail() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(Mono.just(true));
        
        // When
        Mono<UserResponse> result = userService.registerUser(registrationRequest);
        
        // Then
        StepVerifier.create(result)
                .expectError(DuplicateResourceException.class)
                .verify();
        
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void registerUser_DuplicateNit() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(Mono.just(false));
        when(userRepository.existsByNit(anyString())).thenReturn(Mono.just(true));
        
        // When
        Mono<UserResponse> result = userService.registerUser(registrationRequest);
        
        // Then
        StepVerifier.create(result)
                .expectError(DuplicateResourceException.class)
                .verify();
        
        verify(userRepository, times(1)).existsByNit(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void login_Success() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("jwt-token");
        
        // When
        Mono<AuthResponse> result = userService.login(loginRequest);
        
        // Then
        StepVerifier.create(result)
                .expectNextMatches(authResponse ->
                        authResponse.getToken().equals("jwt-token") &&
                        authResponse.getTokenType().equals("Bearer") &&
                        authResponse.getUser().getEmail().equals("juan.perez@example.com"))
                .verifyComplete();
        
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(jwtUtil, times(1)).generateToken(anyString(), anyString());
    }
    
    @Test
    void login_InvalidEmail() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Mono.empty());
        
        // When
        Mono<AuthResponse> result = userService.login(loginRequest);
        
        // Then
        StepVerifier.create(result)
                .expectError(InvalidCredentialsException.class)
                .verify();
        
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
    
    @Test
    void login_InvalidPassword() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        
        // When
        Mono<AuthResponse> result = userService.login(loginRequest);
        
        // Then
        StepVerifier.create(result)
                .expectError(InvalidCredentialsException.class)
                .verify();
        
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }
}

