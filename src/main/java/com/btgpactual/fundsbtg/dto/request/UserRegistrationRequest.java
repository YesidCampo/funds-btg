package com.btgpactual.fundsbtg.dto.request;

import com.btgpactual.fundsbtg.common.constants.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para el registro de un nuevo usuario")
public class UserRegistrationRequest {
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String userName;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
    private String email;
    
    @NotBlank(message = "El NIT es obligatorio")
    @Pattern(regexp = "^[0-9]{9,10}$", message = "El NIT debe tener entre 9 y 10 dígitos")
    @Schema(description = "NIT del usuario", example = "1234567890")
    private String nit;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;
    
    @NotNull(message = "El tipo de notificación preferida es obligatorio")
    @Schema(description = "Tipo de notificación preferida", example = "EMAIL")
    private NotificationType preferredNotification;
    
    @Schema(description = "Número de teléfono (requerido si se selecciona SMS)", example = "3001234567")
    private String phoneNumber;
}
