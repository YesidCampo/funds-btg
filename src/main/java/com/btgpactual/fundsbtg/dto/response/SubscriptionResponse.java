package com.btgpactual.fundsbtg.dto.response;

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
@Schema(description = "Información de una suscripción a un fondo")
public class SubscriptionResponse {
    
    @Schema(description = "ID de la suscripción", example = "507f1f77bcf86cd799439013")
    private String id;
    
    @Schema(description = "ID del fondo", example = "507f1f77bcf86cd799439012")
    private String fundId;
    
    @Schema(description = "ID del usuario", example = "507f1f77bcf86cd799439011")
    private String userId;
    
    @Schema(description = "Nombre técnico del fondo", example = "FPV_BTG_PACTUAL_RECAUDADORA")
    private String fundName;
    
    @Schema(description = "Nombre descriptivo del fondo", example = "BTG Pactual Recaudadora")
    private String fundPrettyName;
    
    @Schema(description = "Monto de inversión", example = "75000.00")
    private BigDecimal investmentAmount;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Fecha de suscripción", example = "2024-01-15T10:30:00")
    private LocalDateTime subscriptionDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Fecha de cancelación", example = "2024-02-15T14:30:00")
    private LocalDateTime cancellationDate;
    
    @Schema(description = "Estado de la suscripción (activa/cancelada)", example = "true")
    private Boolean status;
    
    @Schema(description = "Razón de cancelación", example = "Ya no necesito el servicio")
    private String cancellationReason;
}
