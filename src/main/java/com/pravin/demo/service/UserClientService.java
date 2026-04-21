package com.pravin.demo.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Service
public class UserClientService {

    private final WebClient webClient;

    public UserClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    //Retry → CircuitBreaker → Bulkhead → TimeLimiter
    @Retry(name = "userService")
    @CircuitBreaker(name = "userService", fallbackMethod = "fallback")
    @TimeLimiter(name = "userService")
    @Bulkhead(name = "userService")
    public CompletableFuture<String> getUser() {
        return webClient.get()
                .uri("http://my_user-service/user")
                .retrieve()
                .bodyToMono(String.class)
                .toFuture();
    }

    public CompletableFuture<String> fallback(Throwable ex) {
        return CompletableFuture.completedFuture("UserFallback");
    }
}