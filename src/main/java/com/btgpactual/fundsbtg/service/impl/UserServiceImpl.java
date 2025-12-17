package com.btgpactual.fundsbtg.service.impl;

import com.btgpactual.fundsbtg.common.constants.AppConstants;
import com.btgpactual.fundsbtg.dto.request.LoginRequest;
import com.btgpactual.fundsbtg.dto.request.UserRegistrationRequest;
import com.btgpactual.fundsbtg.dto.response.AuthResponse;
import com.btgpactual.fundsbtg.dto.response.UserResponse;
import com.btgpactual.fundsbtg.exception.DuplicateResourceException;
import com.btgpactual.fundsbtg.exception.InvalidCredentialsException;
import com.btgpactual.fundsbtg.exception.ResourceNotFoundException;
import com.btgpactual.fundsbtg.mapper.UserMapper;
import com.btgpactual.fundsbtg.model.User;
import com.btgpactual.fundsbtg.repository.UserRepository;
import com.btgpactual.fundsbtg.security.JwtUtil;
import com.btgpactual.fundsbtg.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public UserServiceImpl(UserRepository userRepository, 
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    public Mono<UserResponse> registerUser(UserRegistrationRequest request) {
        logger.info("Registrando nuevo usuario con email: {}", request.getEmail());
        
        return validateUserDoesNotExist(request.getEmail(), request.getNit())
                .then(Mono.defer(() -> {
                    String encodedPassword = passwordEncoder.encode(request.getPassword());
                    User user = UserMapper.toEntity(request, encodedPassword);
                    
                    return userRepository.save(user)
                            .map(UserMapper::toResponse)
                            .doOnSuccess(userResponse -> 
                                logger.info("Usuario registrado exitosamente con ID: {}", userResponse.getId()))
                            .doOnError(error -> 
                                logger.error("Error al registrar usuario: {}", error.getMessage()));
                }));
    }
    
    @Override
    public Mono<AuthResponse> login(LoginRequest request) {
        logger.info("Intento de login para email: {}", request.getEmail());
        
        return userRepository.findByEmail(request.getEmail())
                .switchIfEmpty(Mono.error(new InvalidCredentialsException()))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        logger.warn("Contraseña incorrecta para email: {}", request.getEmail());
                        return Mono.error(new InvalidCredentialsException());
                    }
                    
                    if (!Boolean.TRUE.equals(user.getActive())) {
                        logger.warn("Intento de login con usuario inactivo: {}", request.getEmail());
                        return Mono.error(new InvalidCredentialsException("Usuario inactivo"));
                    }
                    
                    String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
                    UserResponse userResponse = UserMapper.toResponse(user);
                    
                    AuthResponse authResponse = AuthResponse.builder()
                            .token(token)
                            .tokenType("Bearer")
                            .user(userResponse)
                            .message("Autenticación exitosa")
                            .build();
                    
                    logger.info("Login exitoso para usuario: {}", user.getEmail());
                    return Mono.just(authResponse);
                });
    }
    
    @Override
    public Mono<UserResponse> getUserById(String userId) {
        logger.info("Buscando usuario con ID: {}", userId);
        
        return userRepository.findById(userId)
                .map(UserMapper::toResponse)
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("Usuario no encontrado con ID: {}", userId);
                    return Mono.error(new ResourceNotFoundException(AppConstants.MSG_USER_NOT_FOUND));
                }));
    }
    
    @Override
    public Mono<UserResponse> getUserByEmail(String email) {
        logger.info("Buscando usuario con email: {}", email);
        
        return userRepository.findByEmail(email)
                .map(UserMapper::toResponse)
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("Usuario no encontrado con email: {}", email);
                    return Mono.error(new ResourceNotFoundException(AppConstants.MSG_USER_NOT_FOUND));
                }));
    }
    
    private Mono<Void> validateUserDoesNotExist(String email, String nit) {
        return userRepository.existsByEmail(email)
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        logger.warn("Intento de registro con email duplicado: {}", email);
                        return Mono.error(new DuplicateResourceException("Usuario", "email", email));
                    }
                    return userRepository.existsByNit(nit);
                })
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        logger.warn("Intento de registro con NIT duplicado: {}", nit);
                        return Mono.error(new DuplicateResourceException("Usuario", "NIT", nit));
                    }
                    return Mono.empty();
                });
    }
}
