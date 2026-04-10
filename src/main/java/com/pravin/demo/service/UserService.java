package com.pravin.demo.service;

import com.pravin.demo.entity.Customer;
import com.pravin.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flux<Customer> getAll() {
        return userRepository.findAll();
    }

    public Mono<Customer> getUserById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("User not found with id: " + id)));
    }

    public Mono<Customer> save(Customer user) {
        return userRepository.findByEmail(user.getEmail())
                // If user exists, throw error
                .flatMap(existingUser -> Mono.<Customer>error(
                        new RuntimeException("Email already in use")))
                // Otherwise, save the user
                .switchIfEmpty(userRepository.save(user));
    }

    public Mono<Customer> updateUser(Long id, Customer user) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("User not found")))
                .flatMap(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setAddress(user.getAddress());
                    existingUser.setCity(user.getCity());
                    existingUser.setZipCode(user.getZipCode());
                    return userRepository.save(existingUser);
                });
    }

    public Mono<Void> deleteUser(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("User not found")))
                .flatMap(userRepository::delete);
    }
}