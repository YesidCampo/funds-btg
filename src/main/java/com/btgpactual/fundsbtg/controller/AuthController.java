package com.btgpactual.fundsbtg.controller;

import com.btgpactual.fundsbtg.common.constants.AppConstants;
import com.btgpactual.fundsbtg.dto.request.LoginRequest;
import com.btgpactual.fundsbtg.dto.request.UserRegistrationRequest;
import com.btgpactual.fundsbtg.dto.response.ApiResponse;
import com.btgpactual.fundsbtg.dto.response.AuthResponse;
import com.btgpactual.fundsbtg.dto.response.UserResponse;
import com.btgpactual.fundsbtg.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(AppConstants.API_V1_AUTH)
@Tag(name = "Autenticación", description = "Endpoints para registro y autenticación de usuarios")
public class AuthController {
    
    private final UserService userService;
    
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Registra un nuevo usuario en el sistema con un saldo inicial de COP $500,000"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Usuario registrado exitosamente",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Email o NIT ya registrado"
        )
    })
    public Mono<ApiResponse<UserResponse>> register(@Valid @RequestBody UserRegistrationRequest request) {
        return userService.registerUser(request)
                .map(user -> ApiResponse.success(AppConstants.MSG_USER_CREATED, user));
    }
    
    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario y devuelve un token JWT válido por 24 horas"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Autenticación exitosa",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Credenciales inválidas"
        )
    })
    public Mono<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
