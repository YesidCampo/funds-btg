package com.btgpactual.fundsbtg.dto.response;

import com.btgpactual.fundsbtg.common.constants.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de un usuario")
public class UserResponse {
    
    @Schema(description = "ID del usuario", example = "507f1f77bcf86cd799439011")
    private String id;
    
    @Schema(description = "Nombre del usuario", example = "Juan Pérez")
    private String userName;
    
    @Schema(description = "Email del usuario", example = "juan.perez@example.com")
    private String email;
    
    @Schema(description = "NIT del usuario", example = "1234567890")
    private String nit;
    
    @Schema(description = "Saldo disponible", example = "500000.00")
    private BigDecimal balance;
    
    @Schema(description = "Rol del usuario", example = "ROLE_USER")
    private String role;
    
    @Schema(description = "Tipo de notificación preferida", example = "EMAIL")
    private NotificationType preferredNotification;
    
    @Schema(description = "Número de teléfono", example = "3001234567")
    private String phoneNumber;
    
    @Schema(description = "Usuario activo", example = "true")
    private Boolean active;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Fecha de creación", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
}
