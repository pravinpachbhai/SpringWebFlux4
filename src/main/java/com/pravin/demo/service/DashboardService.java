package com.pravin.demo.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.pravin.demo.dto.ApiResponse;

@Service
public class DashboardService {

    private final UserClientService userClient;
    private final OrderService orderClient;
    private final RecommendationService recommendationClient;

    public DashboardService(UserClientService user, OrderService order, RecommendationService recommendationClient) {
        this.userClient = user;
        this.orderClient = order;
        this.recommendationClient = recommendationClient;
    }

    public Mono<ApiResponse> getDashboard() {

        Mono<String> user = Mono.fromFuture(userClient.getUser());
        Mono<String> orders = Mono.fromFuture(orderClient.getOrders());
        Mono<String> recs = Mono.fromFuture(recommendationClient.getRecommendations());

        return Mono.zip(user, orders, recs)
                .map(tuple -> new ApiResponse(
                        tuple.getT1(),
                        tuple.getT2(),
                        tuple.getT3()
                ));
    }
}
