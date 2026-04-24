package com.pravin.demo.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {
    private final WebClient webClient;

    public OrderService(WebClient webClient) {
        this.webClient = webClient;
    }

    @CircuitBreaker(name = "orderService", fallbackMethod = "fallback")
    @TimeLimiter(name = "orderService")
    @Bulkhead(name = "orderService")
    public CompletableFuture<String> getOrders() {
        return webClient.get()
                .uri("http://my_order-service/order")
                .retrieve()
                .bodyToMono(String.class)
                .toFuture();
    }

    public CompletableFuture<String> fallback(Throwable ex) {
        return CompletableFuture.completedFuture("OrderFallback");
    }
}
