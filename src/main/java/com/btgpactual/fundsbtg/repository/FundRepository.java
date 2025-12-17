package com.btgpactual.fundsbtg.repository;

import com.btgpactual.fundsbtg.common.constants.FundCategory;
import com.btgpactual.fundsbtg.model.Fund;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FundRepository extends ReactiveMongoRepository<Fund, String> {
    
    Mono<Fund> findByName(String name);
    
    Flux<Fund> findByCategory(FundCategory category);
    
    Flux<Fund> findByActive(Boolean active);
    
    Flux<Fund> findByCategoryAndActive(FundCategory category, Boolean active);
}
