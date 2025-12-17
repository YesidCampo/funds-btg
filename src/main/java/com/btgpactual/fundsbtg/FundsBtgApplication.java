package com.btgpactual.fundsbtg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class FundsBtgApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundsBtgApplication.class, args);
    }
}

