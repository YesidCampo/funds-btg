package com.btgpactual.fundsbtg.service;

import com.btgpactual.fundsbtg.model.Fund;
import com.btgpactual.fundsbtg.model.Subscription;
import com.btgpactual.fundsbtg.model.User;
import reactor.core.publisher.Mono;

public interface NotificationService {
    
    Mono<Void> sendSubscriptionNotification(User user, Fund fund, Subscription subscription);
    
    Mono<Void> sendCancellationNotification(User user, Fund fund, Subscription subscription);
}
