package com.btgpactual.fundsbtg.model;

import com.btgpactual.fundsbtg.common.constants.AppConstants;
import com.btgpactual.fundsbtg.common.constants.NotificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
@Document(collection = AppConstants.COLLECTION_USERS)
public class User {
    
    @Id
    private String id;
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String userName;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Indexed(unique = true)
    private String email;
    
    @NotBlank(message = "El NIT es obligatorio")
    @Pattern(regexp = "^[0-9]{9,10}$", message = "El NIT debe tener entre 9 y 10 dígitos")
    @Indexed(unique = true)
    private String nit;
    
    @NotNull(message = "El saldo es obligatorio")
    @Builder.Default
    private BigDecimal balance = AppConstants.INITIAL_USER_BALANCE;
    
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
    
    @NotNull(message = "El rol es obligatorio")
    @Builder.Default
    private String role = AppConstants.ROLE_USER;
    
    @NotNull(message = "El tipo de notificación es obligatorio")
    @Builder.Default
    private NotificationType preferredNotification = NotificationType.EMAIL;
    
    private String phoneNumber;
    
    @Builder.Default
    private Boolean active = true;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt;
    
    public boolean hasSufficientBalance(BigDecimal amount) {
        return this.balance != null && 
               amount != null && 
               this.balance.compareTo(amount) >= 0;
    }
    
    public void deductBalance(BigDecimal amount) {
        if (!hasSufficientBalance(amount)) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        this.balance = this.balance.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addBalance(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        this.balance = this.balance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }
}
