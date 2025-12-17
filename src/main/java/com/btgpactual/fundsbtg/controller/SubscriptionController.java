package com.btgpactual.fundsbtg.controller;

import com.btgpactual.fundsbtg.common.constants.AppConstants;
import com.btgpactual.fundsbtg.dto.request.SubscriptionRequest;
import com.btgpactual.fundsbtg.dto.response.ApiResponse;
import com.btgpactual.fundsbtg.dto.response.SubscriptionResponse;
import com.btgpactual.fundsbtg.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(AppConstants.API_V1_SUBSCRIPTIONS)
@Tag(name = "Suscripciones", description = "Endpoints para gestionar suscripciones a fondos")
@SecurityRequirement(name = "bearerAuth")
public class SubscriptionController {
    
    private final SubscriptionService subscriptionService;
    
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Suscribirse a un fondo",
        description = "Crea una nueva suscripción a un fondo de inversión. " +
                     "El monto mínimo de inversión será deducido del saldo del usuario. " +
                     "Se enviará una notificación al usuario según su preferencia (EMAIL o SMS)."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Suscripción creada exitosamente",
            content = @Content(schema = @Schema(implementation = SubscriptionResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Usuario o fondo no encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Suscripción duplicada o saldo insuficiente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autorizado"
        )
    })
    public Mono<ApiResponse<SubscriptionResponse>> createSubscription(
            @Valid @RequestBody SubscriptionRequest request) {
        return subscriptionService.createSubscription(request)
                .map(subscription -> ApiResponse.success(
                        AppConstants.MSG_SUBSCRIPTION_CREATED, 
                        subscription));
    }
    
    @PostMapping("/cancel")
    @Operation(
        summary = "Cancelar suscripción a un fondo",
        description = "Cancela una suscripción activa a un fondo. " +
                     "El monto de inversión será devuelto al saldo del usuario. " +
                     "Se enviará una notificación de cancelación al usuario."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Suscripción cancelada exitosamente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Suscripción no encontrada"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autorizado"
        )
    })
    public Mono<ApiResponse<SubscriptionResponse>> cancelSubscription(
            @Valid @RequestBody SubscriptionRequest request) {
        return subscriptionService.cancelSubscription(request)
                .map(subscription -> ApiResponse.success(
                        AppConstants.MSG_SUBSCRIPTION_CANCELLED, 
                        subscription));
    }
    
    @GetMapping("/active/{userId}")
    @Operation(
        summary = "Obtener suscripciones activas",
        description = "Retorna todas las suscripciones activas de un usuario específico"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Suscripciones activas recuperadas exitosamente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autorizado"
        )
    })
    public Mono<ApiResponse<List<SubscriptionResponse>>> getActiveSubscriptions(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable String userId) {
        return subscriptionService.getActiveSubscriptions(userId)
                .collectList()
                .map(subscriptions -> ApiResponse.success(
                        "Suscripciones activas recuperadas exitosamente", 
                        subscriptions));
    }
    
    @GetMapping("/history/{userId}")
    @Operation(
        summary = "Obtener historial de transacciones",
        description = "Retorna el historial completo de suscripciones de un usuario " +
                     "(activas y canceladas). Permite ver todas las aperturas y cancelaciones."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Historial recuperado exitosamente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autorizado"
        )
    })
    public Mono<ApiResponse<List<SubscriptionResponse>>> getSubscriptionHistory(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable String userId) {
        return subscriptionService.getSubscriptionHistory(userId)
                .collectList()
                .map(subscriptions -> ApiResponse.success(
                        "Historial de transacciones recuperado exitosamente", 
                        subscriptions));
    }
    
    @GetMapping("/{subscriptionId}")
    @Operation(
        summary = "Obtener suscripción por ID",
        description = "Retorna la información detallada de una suscripción específica"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Suscripción encontrada"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Suscripción no encontrada"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autorizado"
        )
    })
    public Mono<ApiResponse<SubscriptionResponse>> getSubscriptionById(
            @Parameter(description = "ID de la suscripción", required = true)
            @PathVariable String subscriptionId) {
        return subscriptionService.getSubscriptionById(subscriptionId)
                .map(subscription -> ApiResponse.success("Suscripción encontrada", subscription));
    }
}
