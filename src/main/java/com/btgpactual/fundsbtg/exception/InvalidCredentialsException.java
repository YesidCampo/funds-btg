package com.btgpactual.fundsbtg.exception;

public class InvalidCredentialsException extends BusinessException {
    
    public InvalidCredentialsException(String message) {
        super(message, "INVALID_CREDENTIALS");
    }
    
    public InvalidCredentialsException() {
        super("Email o contraseña incorrectos", "INVALID_CREDENTIALS");
    }
}
