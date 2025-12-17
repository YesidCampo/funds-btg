package com.btgpactual.fundsbtg.model;

import com.btgpactual.fundsbtg.common.constants.AppConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = AppConstants.COLLECTION_SUBSCRIPTIONS)
@CompoundIndexes({
    @CompoundIndex(name = "user_fund_status_idx", 
                   def = "{'userId': 1, 'fundId': 1, 'status': 1}")
})
public class Subscription {
    
    @Id
    private String id;
    
    @NotBlank(message = "El ID del fondo es obligatorio")
    private String fundId;
    
    @NotBlank(message = "El ID del usuario es obligatorio")
    private String userId;
    
    @NotBlank(message = "El nombre del fondo es obligatorio")
    private String fundName;
    
    @NotBlank(message = "El nombre descriptivo del fondo es obligatorio")
    private String fundPrettyName;
    
    @NotNull(message = "El monto de inversión es obligatorio")
    private BigDecimal investmentAmount;
    
    @NotNull(message = "La fecha de suscripción es obligatoria")
    @Builder.Default
    private LocalDateTime subscriptionDate = LocalDateTime.now();
    
    private LocalDateTime cancellationDate;
    
    @NotNull(message = "El estado es obligatorio")
    @Builder.Default
    private Boolean status = true;
    
    private String cancellationReason;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt;
    
    public boolean isActive() {
        return Boolean.TRUE.equals(this.status);
    }
    
    public void cancel(String reason) {
        this.status = false;
        this.cancellationDate = LocalDateTime.now();
        this.cancellationReason = reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getDescription() {
        return String.format("Suscripción al fondo %s por %s %s", 
                           fundPrettyName, 
                           AppConstants.DEFAULT_CURRENCY, 
                           investmentAmount);
    }
}
