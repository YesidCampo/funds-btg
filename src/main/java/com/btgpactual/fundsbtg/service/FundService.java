package com.btgpactual.fundsbtg.service;

import com.btgpactual.fundsbtg.common.constants.FundCategory;
import com.btgpactual.fundsbtg.dto.response.FundResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FundService {
    
    Flux<FundResponse> getAllActiveFunds();
    
    Mono<FundResponse> getFundById(String id);
    
    Flux<FundResponse> getFundsByCategory(FundCategory category);
    
    Flux<FundResponse> getAllFunds();
}
