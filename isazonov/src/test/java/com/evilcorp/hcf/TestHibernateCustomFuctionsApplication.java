package com.evilcorp.hcf;

import org.springframework.boot.SpringApplication;

public class TestHibernateCustomFuctionsApplication {

    public static void main(String[] args) {
        SpringApplication.from(HibernateCustomFunctionsApplication::main).with(PostgresTestcontainersConfiguration.class).run(args);
    }

}
