package com.btgpactual.fundsbtg.service;

import com.btgpactual.fundsbtg.dto.request.SubscriptionRequest;
import com.btgpactual.fundsbtg.dto.response.SubscriptionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SubscriptionService {
    
    Mono<SubscriptionResponse> createSubscription(SubscriptionRequest request);
    
    Mono<SubscriptionResponse> cancelSubscription(SubscriptionRequest request);
    
    Flux<SubscriptionResponse> getActiveSubscriptions(String userId);
    
    Flux<SubscriptionResponse> getSubscriptionHistory(String userId);
    
    Mono<SubscriptionResponse> getSubscriptionById(String subscriptionId);
}
