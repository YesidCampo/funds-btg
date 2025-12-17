package com.btgpactual.fundsbtg.mapper;

import com.btgpactual.fundsbtg.dto.response.SubscriptionResponse;
import com.btgpactual.fundsbtg.model.Subscription;

public class SubscriptionMapper {
    
    private SubscriptionMapper() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no puede ser instanciada");
    }
    
    public static SubscriptionResponse toResponse(Subscription subscription) {
        if (subscription == null) {
            return null;
        }
        
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .fundId(subscription.getFundId())
                .userId(subscription.getUserId())
                .fundName(subscription.getFundName())
                .fundPrettyName(subscription.getFundPrettyName())
                .investmentAmount(subscription.getInvestmentAmount())
                .subscriptionDate(subscription.getSubscriptionDate())
                .cancellationDate(subscription.getCancellationDate())
                .status(subscription.getStatus())
                .cancellationReason(subscription.getCancellationReason())
                .build();
    }
}
