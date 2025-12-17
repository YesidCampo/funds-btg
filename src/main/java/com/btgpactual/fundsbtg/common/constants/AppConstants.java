package com.btgpactual.fundsbtg.common.constants;

import java.math.BigDecimal;

public final class AppConstants {
    
    public static final BigDecimal INITIAL_USER_BALANCE = new BigDecimal("500000");
    public static final String DEFAULT_CURRENCY = "COP";
    
    public static final String MSG_INSUFFICIENT_BALANCE = "No tiene saldo disponible para vincularse al fondo %s";
    public static final String MSG_SUBSCRIPTION_EXISTS = "El usuario ya tiene una suscripción activa a este fondo";
    public static final String MSG_SUBSCRIPTION_NOT_FOUND = "No se encontró la suscripción activa";
    public static final String MSG_USER_NOT_FOUND = "Usuario no encontrado";
    public static final String MSG_FUND_NOT_FOUND = "Fondo no encontrado";
    public static final String MSG_INVALID_CREDENTIALS = "Credenciales inválidas";
    
    public static final String MSG_SUBSCRIPTION_CREATED = "Suscripción creada exitosamente";
    public static final String MSG_SUBSCRIPTION_CANCELLED = "Suscripción cancelada exitosamente";
    public static final String MSG_USER_CREATED = "Usuario creado exitosamente";
    
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    
    public static final String JWT_SECRET_KEY = "btg.pactual.funds.jwt.secret.key";
    public static final long JWT_EXPIRATION_TIME = 86400000;
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_HEADER_STRING = "Authorization";
    
    public static final String API_V1_BASE_PATH = "/api/v1";
    public static final String API_V1_FUNDS = API_V1_BASE_PATH + "/funds";
    public static final String API_V1_SUBSCRIPTIONS = API_V1_BASE_PATH + "/subscriptions";
    public static final String API_V1_USERS = API_V1_BASE_PATH + "/users";
    public static final String API_V1_AUTH = API_V1_BASE_PATH + "/auth";
    
    public static final String NOTIFICATION_EMAIL = "EMAIL";
    public static final String NOTIFICATION_SMS = "SMS";
    
    public static final String EMAIL_TEMPLATE_SUBSCRIPTION = "subscription-template";
    public static final String EMAIL_TEMPLATE_CANCELLATION = "unsubscribe-template";
    public static final String EMAIL_SUBJECT_SUBSCRIPTION = "Suscripción a Fondo BTG Pactual";
    public static final String EMAIL_SUBJECT_CANCELLATION = "Cancelación de Suscripción a Fondo BTG Pactual";
    
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_FUNDS = "funds";
    public static final String COLLECTION_SUBSCRIPTIONS = "subscriptions";
    
    private AppConstants() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no puede ser instanciada");
    }
}
