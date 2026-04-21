package com.pravin.demo.controller;

import com.pravin.demo.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pravin.demo.dto.ApiResponse;
import reactor.core.publisher.Mono;

@RestController
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/dashboard")
    public Mono<ApiResponse> dashboard() {
        return service.getDashboard();
    }
}