package com.btgpactual.fundsbtg.controller;

import com.btgpactual.fundsbtg.common.constants.AppConstants;
import com.btgpactual.fundsbtg.dto.response.ApiResponse;
import com.btgpactual.fundsbtg.dto.response.UserResponse;
import com.btgpactual.fundsbtg.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(AppConstants.API_V1_USERS)
@Tag(name = "Usuarios", description = "Endpoints para gestionar información de usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/{userId}")
    @Operation(
        summary = "Obtener usuario por ID",
        description = "Retorna la información completa de un usuario incluyendo su saldo actual"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autorizado"
        )
    })
    public Mono<ApiResponse<UserResponse>> getUserById(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable String userId) {
        return userService.getUserById(userId)
                .map(user -> ApiResponse.success("Usuario encontrado", user));
    }
    
    @GetMapping("/email/{email}")
    @Operation(
        summary = "Obtener usuario por email",
        description = "Retorna la información de un usuario buscando por su email"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autorizado"
        )
    })
    public Mono<ApiResponse<UserResponse>> getUserByEmail(
            @Parameter(description = "Email del usuario", required = true)
            @PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(user -> ApiResponse.success("Usuario encontrado", user));
    }
}
