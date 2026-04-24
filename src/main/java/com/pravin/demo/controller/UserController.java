package com.pravin.demo.controller;

import com.pravin.demo.entity.Customer;
import com.pravin.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Flux<Customer> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Customer> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Customer> createUser(@Valid @RequestBody Customer user) {
        return userService.save(user);
    }

    @PutMapping("/{id}")
    public Mono<Customer> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody Customer user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

}
