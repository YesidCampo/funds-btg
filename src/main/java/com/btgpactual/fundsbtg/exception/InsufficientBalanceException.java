package com.btgpactual.fundsbtg.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends BusinessException {
    
    private final BigDecimal currentBalance;
    private final BigDecimal requiredAmount;
    
    public InsufficientBalanceException(String message, BigDecimal currentBalance, BigDecimal requiredAmount) {
        super(message, "INSUFFICIENT_BALANCE");
        this.currentBalance = currentBalance;
        this.requiredAmount = requiredAmount;
    }
    
    public static InsufficientBalanceException forFund(String fundName, BigDecimal currentBalance, BigDecimal requiredAmount) {
        String message = String.format("No tiene saldo disponible para vincularse al fondo %s. Saldo actual: %s, Requerido: %s",
                                      fundName, currentBalance, requiredAmount);
        return new InsufficientBalanceException(message, currentBalance, requiredAmount);
    }
    
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }
    
    public BigDecimal getRequiredAmount() {
        return requiredAmount;
    }
}
