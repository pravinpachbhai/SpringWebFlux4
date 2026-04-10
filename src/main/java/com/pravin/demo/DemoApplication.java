package com.pravin.demo;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.web.reactive.config.EnableWebFlux;

/*
 http://localhost:8080/api-docs
 http://localhost:8080/swagger-ui/index.html
 http://localhost:8080/actuator/circuitbreakers
 http://localhost:8080/actuator/health
 http://localhost:8080/actuator/metrics/resilience4j.circuitbreaker.calls
 http://localhost:8080/actuator/ratelimiters
 http://localhost:8080/actuator/bulkheads
 http://localhost:8080/actuator/metrics/resilience4j.ratelimiter.calls
 http://localhost:8080/actuator/prometheus

 */


@SpringBootApplication
@EnableWebFlux
public class DemoApplication {

    static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));

        return initializer;
    }

}
