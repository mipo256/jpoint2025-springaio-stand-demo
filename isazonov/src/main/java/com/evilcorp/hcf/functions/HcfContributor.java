package com.evilcorp.hcf.functions;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;

public class HcfContributor implements FunctionContributor {
    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        functionContributions.getFunctionRegistry()
                .registerPattern("lt_between_dates", "?1 - ?2 < interval ?3"
                        , functionContributions.getTypeConfiguration()
                                .getBasicTypeForJavaType(Boolean.class));

        functionContributions.getFunctionRegistry()
                .registerPattern("lt_interval", "?1 < ?2"
                        , functionContributions.getTypeConfiguration()
                                .getBasicTypeForJavaType(Boolean.class));

        functionContributions.getFunctionRegistry()
                .registerPattern("lt_interval_par", "?1 < make_interval(0, 0, 0, 0, 0, 0, ?2)"
                        , functionContributions.getTypeConfiguration()
                                .getBasicTypeForJavaType(Boolean.class));

        functionContributions.getFunctionRegistry()
                .registerPattern("between_dates", "?1 - ?2");

        functionContributions.getFunctionRegistry()
                .register("lt_interval_func", new LtIntervalFunction("lt_interval_func"))
        ;
    }
}
