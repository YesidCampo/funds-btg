package com.btgpactual.fundsbtg.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API de Gestión de Fondos BTG Pactual",
        version = "1.0.0",
        description = "API REST para la gestión de fondos de inversión. " +
                     "Permite a los clientes suscribirse a fondos, cancelar suscripciones " +
                     "y consultar su historial de transacciones sin necesidad de contactar a un asesor."
    ),
    servers = {
        @Server(
            description = "Servidor Local",
            url = "http://localhost:8080"
        )
    }
)
@SecurityScheme(
    name = "bearerAuth",
    description = "Autenticación JWT. Incluir el token en el header Authorization con el prefijo 'Bearer '",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
