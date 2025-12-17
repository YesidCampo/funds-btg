package com.btgpactual.fundsbtg.repository;

import com.btgpactual.fundsbtg.model.Subscription;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SubscriptionRepository extends ReactiveMongoRepository<Subscription, String> {
    
    Flux<Subscription> findByUserId(String userId);
    
    Flux<Subscription> findByUserIdAndStatus(String userId, Boolean status);
    
    Mono<Subscription> findByUserIdAndFundIdAndStatus(String userId, String fundId, Boolean status);
    
    Flux<Subscription> findByFundId(String fundId);
    
    Flux<Subscription> findByFundIdAndStatus(String fundId, Boolean status);
    
    Mono<Boolean> existsByUserIdAndFundIdAndStatus(String userId, String fundId, Boolean status);
}
