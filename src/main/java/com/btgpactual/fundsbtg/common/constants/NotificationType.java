package com.btgpactual.fundsbtg.common.constants;

public enum NotificationType {
    EMAIL("Correo Electrónico"),
    SMS("Mensaje de Texto");
    
    private final String description;
    
    NotificationType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
