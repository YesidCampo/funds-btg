package com.btgpactual.fundsbtg.service.impl;

import com.btgpactual.fundsbtg.common.constants.AppConstants;
import com.btgpactual.fundsbtg.common.constants.FundCategory;
import com.btgpactual.fundsbtg.dto.response.FundResponse;
import com.btgpactual.fundsbtg.exception.ResourceNotFoundException;
import com.btgpactual.fundsbtg.mapper.FundMapper;
import com.btgpactual.fundsbtg.repository.FundRepository;
import com.btgpactual.fundsbtg.service.FundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FundServiceImpl implements FundService {
    
    private static final Logger logger = LoggerFactory.getLogger(FundServiceImpl.class);
    
    private final FundRepository fundRepository;
    
    public FundServiceImpl(FundRepository fundRepository) {
        this.fundRepository = fundRepository;
    }
    
    @Override
    public Flux<FundResponse> getAllActiveFunds() {
        logger.info("Obteniendo todos los fondos activos");
        
        return fundRepository.findByActive(true)
                .map(FundMapper::toResponse)
                .doOnComplete(() -> logger.info("Fondos activos recuperados exitosamente"))
                .doOnError(error -> logger.error("Error al obtener fondos activos: {}", error.getMessage()));
    }
    
    @Override
    public Mono<FundResponse> getFundById(String id) {
        logger.info("Buscando fondo con ID: {}", id);
        
        return fundRepository.findById(id)
                .map(FundMapper::toResponse)
                .doOnSuccess(fund -> logger.info("Fondo encontrado: {}", fund.getName()))
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("Fondo no encontrado con ID: {}", id);
                    return Mono.error(new ResourceNotFoundException(
                            AppConstants.MSG_FUND_NOT_FOUND));
                }));
    }
    
    @Override
    public Flux<FundResponse> getFundsByCategory(FundCategory category) {
        logger.info("Buscando fondos de categoría: {}", category);
        
        return fundRepository.findByCategoryAndActive(category, true)
                .map(FundMapper::toResponse)
                .doOnComplete(() -> logger.info("Fondos de categoría {} recuperados", category))
                .doOnError(error -> logger.error("Error al obtener fondos por categoría: {}", error.getMessage()));
    }
    
    @Override
    public Flux<FundResponse> getAllFunds() {
        logger.info("Obteniendo todos los fondos");
        
        return fundRepository.findAll()
                .map(FundMapper::toResponse)
                .doOnComplete(() -> logger.info("Todos los fondos recuperados exitosamente"))
                .doOnError(error -> logger.error("Error al obtener todos los fondos: {}", error.getMessage()));
    }
}
