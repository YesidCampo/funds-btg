package com.btgpactual.fundsbtg.repository;

import com.btgpactual.fundsbtg.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    
    Mono<User> findByEmail(String email);
    
    Mono<User> findByNit(String nit);
    
    Mono<Boolean> existsByEmail(String email);
    
    Mono<Boolean> existsByNit(String nit);
}
