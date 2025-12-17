package com.btgpactual.fundsbtg.dto.response;

import com.btgpactual.fundsbtg.common.constants.FundCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de un fondo de inversión")
public class FundResponse {
    
    @Schema(description = "ID del fondo", example = "507f1f77bcf86cd799439012")
    private String id;
    
    @Schema(description = "Nombre técnico del fondo", example = "FPV_BTG_PACTUAL_RECAUDADORA")
    private String name;
    
    @Schema(description = "Nombre descriptivo del fondo", example = "BTG Pactual Recaudadora")
    private String prettyName;
    
    @Schema(description = "Monto mínimo de inversión", example = "75000.00")
    private BigDecimal minimumInvestmentAmount;
    
    @Schema(description = "Moneda", example = "COP")
    private String currency;
    
    @Schema(description = "Categoría del fondo", example = "FPV")
    private FundCategory category;
    
    @Schema(description = "Fondo activo", example = "true")
    private Boolean active;
}
