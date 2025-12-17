package com.btgpactual.fundsbtg.exception;

public class DuplicateResourceException extends BusinessException {
    
    public DuplicateResourceException(String message) {
        super(message, "DUPLICATE_RESOURCE");
    }
    
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s ya existe con %s : '%s'", resourceName, fieldName, fieldValue),
              "DUPLICATE_RESOURCE");
    }
}
