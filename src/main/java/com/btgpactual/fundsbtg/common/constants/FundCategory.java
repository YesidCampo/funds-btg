package com.btgpactual.fundsbtg.common.constants;

public enum FundCategory {
    FPV("Fondo de Pensiones Voluntarias"),
    FIC("Fondo de Inversión Colectiva");
    
    private final String description;
    
    FundCategory(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
