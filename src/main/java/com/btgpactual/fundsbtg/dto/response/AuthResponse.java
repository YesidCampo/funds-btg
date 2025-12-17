package com.btgpactual.fundsbtg.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de autenticación con token JWT")
public class AuthResponse {
    
    @Schema(description = "Token JWT de acceso", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "Tipo de token", example = "Bearer")
    private String tokenType;
    
    @Schema(description = "Información del usuario autenticado")
    private UserResponse user;
    
    @Schema(description = "Mensaje de éxito", example = "Autenticación exitosa")
    private String message;
}
