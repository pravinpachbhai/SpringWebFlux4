package com.pravin.demo.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
public class RecommendationService {
    private final WebClient webClient;

    public RecommendationService(WebClient webClient) {
        this.webClient = webClient;
    }

    @CircuitBreaker(name = "recommendationService", fallbackMethod = "fallback")
    @TimeLimiter(name = "recommendationService")
    @Bulkhead(name = "recommendationService")
    public CompletableFuture<String> getRecommendations() {
        return webClient.get()
                .uri("http://my_recommendation-service/recommendation")
                .retrieve()
                .bodyToMono(String.class)
                .toFuture();
    }

    public CompletableFuture<String> fallback(Throwable ex) {
        return CompletableFuture.completedFuture("RecommendationFallback");
    }
}
