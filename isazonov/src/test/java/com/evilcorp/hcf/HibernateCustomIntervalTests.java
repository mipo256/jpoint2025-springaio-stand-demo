package com.evilcorp.hcf;

import com.evilcorp.hcf.entities.Product;
import com.evilcorp.hcf.repositories.interval.ProductIntervalRepository;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(PostgresTestcontainersConfiguration.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernateCustomIntervalTests {

    @Autowired
    private ProductIntervalRepository productRepository;

    @PostConstruct
    void init() {
        final Product piledriver = productRepository.save(Product.builder()
                .name("Piledriver")
                .price(16)
                .startedSales(LocalDateTime.of(2020, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .endedSales(LocalDateTime.of(2022, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .build());

        final Product screwDriver = productRepository.save(Product.builder()
                .name("screwdriver")
                .price(16)
                .startedSales(LocalDateTime.of(2020, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .endedSales(LocalDateTime.of(2020, 2, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .build());
    }

    @Test
    void findProductById() {
        final Product expected = Product.builder()
                .name("screwdriver")
                .price(16)
                .startedSales(LocalDateTime.of(2020, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
//                .endedSales(LocalDateTime.of(2022, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .endedSales(LocalDateTime.of(2020, 2, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .build();

        final List<Product> sales = productRepository.findYearLongSalesNative();

        final Product actual = sales.iterator().next();

        assertThat(sales.size()).isEqualTo(1);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    void findProductByIntervalId() {
        final Product expected = Product.builder()
                .name("screwdriver")
                .price(16)
                .startedSales(LocalDateTime.of(2020, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
//                .endedSales(LocalDateTime.of(2022, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .endedSales(LocalDateTime.of(2020, 2, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .build();

        final List<Product> sales = productRepository.findYearLongSalesInlineSql();
        final Product actual = sales.iterator().next();

        assertThat(sales.size()).isEqualTo(1);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    void findProductByIntervalIdInlineSql() {
        final Product expected = Product.builder()
                .name("screwdriver")
                .price(16)
                .startedSales(LocalDateTime.of(2020, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
//                .endedSales(LocalDateTime.of(2022, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .endedSales(LocalDateTime.of(2020, 2, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .build();

        final List<Product> sales = productRepository.findYearLongSalesSimplePattern();

        final Product actual = sales.iterator().next();

        assertThat(sales.size()).isEqualTo(1);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    void findProductByIntervalIdInlineSqlCun() {
        final Product expected = Product.builder()
                .name("screwdriver")
                .price(16)
                .startedSales(LocalDateTime.of(2020, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
//                .endedSales(LocalDateTime.of(2022, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .endedSales(LocalDateTime.of(2020, 2, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .build();

        final List<Product> sales = productRepository.findYearLongSalesPatternInPattern();

        final Product actual = sales.iterator().next();

        assertThat(sales.size()).isEqualTo(1);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    void findProductByIntervalIdInlineSqlCunPeriod() {
        final Product expected = Product.builder()
                .name("screwdriver")
                .price(16)
                .startedSales(LocalDateTime.of(2020, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
//                .endedSales(LocalDateTime.of(2022, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .endedSales(LocalDateTime.of(2020, 2, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .build();

        final List<Product> sales = productRepository.findYearLongSalesPatternParameter(
                Duration.ofDays(365).toSeconds());

        final Product actual = sales.iterator().next();

        assertThat(sales.size()).isEqualTo(1);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    void findProductByIntervalIdInlineSqlCunPeriodFc() {
        final Product expected = Product.builder()
                .name("screwdriver")
                .price(16)
                .startedSales(LocalDateTime.of(2020, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
//                .endedSales(LocalDateTime.of(2022, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .endedSales(LocalDateTime.of(2020, 2, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .build();

        final List<Product> sales = productRepository.findYearLongSalesFunctionParameter(
                Duration.ofDays(365).toSeconds());

        final Product actual = sales.iterator().next();

        assertThat(sales.size()).isEqualTo(1);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    void findProductByIntervalIdInlineSqlCunPeriodFcLit() {
        final Product expected = Product.builder()
                .name("screwdriver")
                .price(16)
                .startedSales(LocalDateTime.of(2020, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
//                .endedSales(LocalDateTime.of(2022, 1, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .endedSales(LocalDateTime.of(2020, 2, 1, 0, 0).atOffset(ZoneOffset.UTC))
                .build();

        final List<Product> sales = productRepository.findYearLongSalesFunctionLiteral();
        final Product actual = sales.iterator().next();

        assertThat(sales.size()).isEqualTo(1);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }
}
