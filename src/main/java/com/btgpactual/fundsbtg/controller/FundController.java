package com.btgpactual.fundsbtg.controller;

import com.btgpactual.fundsbtg.common.constants.AppConstants;
import com.btgpactual.fundsbtg.common.constants.FundCategory;
import com.btgpactual.fundsbtg.dto.response.ApiResponse;
import com.btgpactual.fundsbtg.dto.response.FundResponse;
import com.btgpactual.fundsbtg.service.FundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(AppConstants.API_V1_FUNDS)
@Tag(name = "Fondos", description = "Endpoints para gestionar fondos de inversión")
@SecurityRequirement(name = "bearerAuth")
public class FundController {
    
    private final FundService fundService;
    
    public FundController(FundService fundService) {
        this.fundService = fundService;
    }
    
    @GetMapping
    @Operation(
        summary = "Obtener todos los fondos activos",
        description = "Retorna la lista de todos los fondos de inversión activos disponibles para suscripción"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Lista de fondos recuperada exitosamente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autorizado - Token JWT inválido o ausente"
        )
    })
    public Mono<ApiResponse<List<FundResponse>>> getAllActiveFunds() {
        return fundService.getAllActiveFunds()
                .collectList()
                .map(funds -> ApiResponse.success("Fondos recuperados exitosamente", funds));
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener fondo por ID",
        description = "Retorna la información detallada de un fondo específico"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Fondo encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Fondo no encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autorizado"
        )
    })
    public Mono<ApiResponse<FundResponse>> getFundById(
            @Parameter(description = "ID del fondo", required = true)
            @PathVariable String id) {
        return fundService.getFundById(id)
                .map(fund -> ApiResponse.success("Fondo encontrado", fund));
    }
    
    @GetMapping("/category/{category}")
    @Operation(
        summary = "Obtener fondos por categoría",
        description = "Retorna la lista de fondos de una categoría específica (FPV o FIC)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Fondos de la categoría recuperados exitosamente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autorizado"
        )
    })
    public Mono<ApiResponse<List<FundResponse>>> getFundsByCategory(
            @Parameter(description = "Categoría del fondo (FPV o FIC)", required = true)
            @PathVariable FundCategory category) {
        return fundService.getFundsByCategory(category)
                .collectList()
                .map(funds -> ApiResponse.success(
                        String.format("Fondos de categoría %s recuperados exitosamente", category), 
                        funds));
    }
}
