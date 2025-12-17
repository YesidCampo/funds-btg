package com.btgpactual.fundsbtg.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de error de la API")
public class ErrorResponse {
    
    @Schema(description = "Código de estado HTTP", example = "400")
    private int status;
    
    @Schema(description = "Código de error específico de la aplicación", example = "INSUFFICIENT_BALANCE")
    private String errorCode;
    
    @Schema(description = "Mensaje de error principal", example = "No tiene saldo suficiente")
    private String message;
    
    @Schema(description = "Detalles adicionales del error")
    private List<String> details;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Timestamp del error", example = "2024-01-15T10:30:00")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    @Schema(description = "Ruta del endpoint donde ocurrió el error", example = "/api/v1/subscriptions")
    private String path;
    
    public ErrorResponse(int status, String errorCode, String message, String path) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
