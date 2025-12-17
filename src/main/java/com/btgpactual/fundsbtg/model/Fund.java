package com.btgpactual.fundsbtg.model;

import com.btgpactual.fundsbtg.common.constants.AppConstants;
import com.btgpactual.fundsbtg.common.constants.FundCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = AppConstants.COLLECTION_FUNDS)
public class Fund {
    
    @Id
    private String id;
    
    @NotBlank(message = "El nombre del fondo es obligatorio")
    @Indexed(unique = true)
    private String name;
    
    @NotBlank(message = "El nombre descriptivo es obligatorio")
    private String prettyName;
    
    @NotNull(message = "El monto mínimo de inversión es obligatorio")
    @Positive(message = "El monto mínimo debe ser positivo")
    private BigDecimal minimumInvestmentAmount;
    
    @NotBlank(message = "La moneda es obligatoria")
    @Builder.Default
    private String currency = AppConstants.DEFAULT_CURRENCY;
    
    @NotNull(message = "La categoría es obligatoria")
    private FundCategory category;
    
    @Builder.Default
    private Boolean active = true;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt;
    
    public boolean meetsMinimumInvestment(BigDecimal amount) {
        return amount != null && 
               this.minimumInvestmentAmount != null && 
               amount.compareTo(this.minimumInvestmentAmount) >= 0;
    }
    
    public String getFormattedName() {
        return String.format("%s (%s)", prettyName, name);
    }
}
