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
@Schema(description = "Datos para suscribirse o cancelar suscripción a un fondo")
public class SubscriptionRequest {
    
    @NotBlank(message = "El ID del usuario es obligatorio")
    @Schema(description = "ID del usuario", example = "507f1f77bcf86cd799439011")
    private String userId;
    
    @NotBlank(message = "El ID del fondo es obligatorio")
    @Schema(description = "ID del fondo", example = "507f1f77bcf86cd799439012")
    private String fundId;
    
    @Schema(description = "Razón de cancelación (solo para cancelaciones)", example = "Ya no necesito el servicio")
    private String cancellationReason;
}
