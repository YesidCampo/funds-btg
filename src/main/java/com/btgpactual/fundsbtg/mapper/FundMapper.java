package com.btgpactual.fundsbtg.mapper;

import com.btgpactual.fundsbtg.dto.response.FundResponse;
import com.btgpactual.fundsbtg.model.Fund;

public class FundMapper {
    
    private FundMapper() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no puede ser instanciada");
    }
    
    public static FundResponse toResponse(Fund fund) {
        if (fund == null) {
            return null;
        }
        
        return FundResponse.builder()
                .id(fund.getId())
                .name(fund.getName())
                .prettyName(fund.getPrettyName())
                .minimumInvestmentAmount(fund.getMinimumInvestmentAmount())
                .currency(fund.getCurrency())
                .category(fund.getCategory())
                .active(fund.getActive())
                .build();
    }
}
