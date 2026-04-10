package com.pravin.demo.repository;

import com.pravin.demo.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<Customer, Long> {
    Flux<Customer> findByName(String name);

    Mono<Customer> findByEmail(String email);
}