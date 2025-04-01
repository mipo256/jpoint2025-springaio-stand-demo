package com.evilcorp.hcf.functions;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;

/**
 * This class is needed to add custom functions and patterns to Hibernate
 * <p/>
 * It uses standard Hibernate 6 api for extension.
 * <p/>
 * Following patters are added.
 * <p/>
 * lt_between_dates(:endDate, :startDate, :interval)
 * :interval should be a part of PostgreSql interval literal, with interval
 * keyword omitted.
 * returns true if interval between dates is shorted than referenceInterval
 * <p/>
 * lt_interval(:firstInterval, :secondInterval)
 * Parameters should be a part of PostgreSql interval literal, with interval
 * keyword omitted
 * returns true, if first interval is shorter, than the second one
 * <p/>
 * lt_interval_par(:interval, :seconds)
 * :interval is Postgresql interval, :seconds is bigint
 * returns true if :interval is shorter, than :seconds, converted to PostgreSql
 * interval.
 * <p/>
 * This function is needed, because lt_interval can not accept second argument
 * as an SQL parameter
 * <p/>
 * between_dates(:endDate, :starDate) - returns Postgresql interval, between
 * :startDate and :endDate.
 * <p/>
 * lt_interval_func(:firstInterval, :secondInterval)
 * :secondInterval can either be PostgeSql interval literal with interval keyword
 * omitted, or it can be SQL biging parameter, containing number of seconds in
 * an interval.
 * returns true if :firstInterval is shorter, than :secondInterval
 * </p>
 * Can accept either literal or SQL parameter, making lt_interval_par and
 * lt_interval obsolete
 */
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
