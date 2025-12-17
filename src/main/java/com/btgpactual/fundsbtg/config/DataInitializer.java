package com.btgpactual.fundsbtg.config;

import com.btgpactual.fundsbtg.common.constants.FundCategory;
import com.btgpactual.fundsbtg.model.Fund;
import com.btgpactual.fundsbtg.repository.FundRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    private final FundRepository fundRepository;
    
    public DataInitializer(FundRepository fundRepository) {
        this.fundRepository = fundRepository;
    }
    
    @Override
    public void run(String... args) {
        logger.info("Inicializando datos de fondos...");
        
        fundRepository.count()
                .flatMapMany(count -> {
                    if (count > 0) {
                        logger.info("Los fondos ya existen en la base de datos. Total: {}", count);
                        return Flux.empty();
                    }
                    
                    logger.info("Creando fondos iniciales...");
                    return Flux.just(
                            Fund.builder()
                                    .name("FPV_BTG_PACTUAL_RECAUDADORA")
                                    .prettyName("BTG Pactual Recaudadora")
                                    .minimumInvestmentAmount(new BigDecimal("75000"))
                                    .currency("COP")
                                    .category(FundCategory.FPV)
                                    .active(true)
                                    .build(),
                            
                            Fund.builder()
                                    .name("FPV_BTG_PACTUAL_ECOPETROL")
                                    .prettyName("BTG Pactual Ecopetrol")
                                    .minimumInvestmentAmount(new BigDecimal("125000"))
                                    .currency("COP")
                                    .category(FundCategory.FPV)
                                    .active(true)
                                    .build(),
                            
                            Fund.builder()
                                    .name("DEUDAPRIVADA")
                                    .prettyName("Deuda Privada")
                                    .minimumInvestmentAmount(new BigDecimal("50000"))
                                    .currency("COP")
                                    .category(FundCategory.FIC)
                                    .active(true)
                                    .build(),
                            
                            Fund.builder()
                                    .name("FDO-ACCIONES")
                                    .prettyName("Fondo de Acciones")
                                    .minimumInvestmentAmount(new BigDecimal("250000"))
                                    .currency("COP")
                                    .category(FundCategory.FIC)
                                    .active(true)
                                    .build(),
                            
                            Fund.builder()
                                    .name("FPV_BTG_PACTUAL_DINAMICA")
                                    .prettyName("BTG Pactual Dinámica")
                                    .minimumInvestmentAmount(new BigDecimal("100000"))
                                    .currency("COP")
                                    .category(FundCategory.FPV)
                                    .active(true)
                                    .build()
                    );
                })
                .flatMap(fundRepository::save)
                .doOnNext(fund -> logger.info("Fondo creado: {} - {}", fund.getName(), fund.getPrettyName()))
                .doOnComplete(() -> logger.info("Inicialización de fondos completada"))
                .doOnError(error -> logger.error("Error al inicializar fondos: {}", error.getMessage()))
                .subscribe();
    }
}
