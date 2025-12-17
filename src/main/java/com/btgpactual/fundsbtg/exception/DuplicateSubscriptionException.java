package com.btgpactual.fundsbtg.exception;

public class DuplicateSubscriptionException extends BusinessException {
    
    public DuplicateSubscriptionException(String message) {
        super(message, "DUPLICATE_SUBSCRIPTION");
    }
    
    public DuplicateSubscriptionException(String userId, String fundId) {
        super(String.format("El usuario %s ya tiene una suscripción activa al fondo %s", userId, fundId),
              "DUPLICATE_SUBSCRIPTION");
    }
}
