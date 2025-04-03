package com.evilcorp.hcf.repositories.interval;

import com.evilcorp.hcf.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductIntervalRepository extends JpaRepository<Product, UUID> {
    @Query(value = """
        select
          p.product_id,
          p.name,
          p.price,
          p.started_sales,  
          p.ended_sales,
          p.tags  
        from
          products p    
        where
          (p.ended_sales - p.started_sales) < interval '1 year'
    """
            , nativeQuery = true
    )
    List<Product> findYearLongSalesNative();

    @Query(value = """
        select
          p
        from
          Product p
        where
          cast(
            sql('(? - ? < interval ?)', p.endedSales, p.startedSales, "1 year")
          as boolean)
    """
    )
    List<Product> findYearLongSalesInlineSql();

    @Query(value = """
        select
          p
        from
          Product p
        where
          lt_between_dates(p.endedSales, p.startedSales, '1 year')
   """
    )
    List<Product> findYearLongSalesSimplePattern();

    @Query(value = """
        select
          p
        from
          Product p
        where
          lt_interval(between_dates(p.endedSales, p.startedSales), '1 year')
   """)
    List<Product> findYearLongSalesPatternInPattern();

    @Query(value = """
        select
          p
        from
          Product p
        where
          lt_interval_par(between_dates(p.endedSales, p.startedSales), :salesPeriod)
   """)
    List<Product> findYearLongSalesPatternParameter(Long salesPeriod);

    @Query(value = """
        select
          p
        from
          Product p
        where
          lt_interval_func(between_dates(p.endedSales, p.startedSales), :salesPeriod)
   """)
    List<Product> findYearLongSalesFunctionParameter(Long salesPeriod);

    @Query(value = """
        select
          p
        from
          Product p
        where
          lt_interval_func(between_dates(p.endedSales, p.startedSales), '1 year' )
    """)
    List<Product> findYearLongSalesFunctionLiteral();
}
