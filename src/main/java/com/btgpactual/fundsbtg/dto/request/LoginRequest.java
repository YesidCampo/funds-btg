package com.btgpactual.fundsbtg.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Credenciales de inicio de sesión")
public class LoginRequest {
    
    @NotBlank(message = "El email es obligatorio")
    @Schema(description = "Email del usuario", example = "juan.perez@example.com")
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;
}
