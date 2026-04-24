package com.pravin.demo.controller;

import com.pravin.demo.entity.Customer;
import com.pravin.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

import java.util.Objects;

@SpringBootTest
@AutoConfigureWebTestClient
public class UserControllerTest {

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetAllUsers() {
        webTestClient.get()
                .uri("/api/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Customer.class)
                .hasSize(0); // Assuming empty DB
    }

    @Test
    void testCreateAndRetrieveUser() {
        Customer newUser = new Customer();
        newUser.setName("Pravin");
        newUser.setCity("St Louis");
        newUser.setAddress("1234 Test Drive");
        newUser.setZipCode("63131");
        newUser.setEmail("pravin_pachbhai@yahoo.com");
        Customer savedUser = new Customer();
        savedUser.setId(1);
        savedUser.setName("Pravin");
        savedUser.setCity("St Louis");
        savedUser.setAddress("1234 Test Drive");
        savedUser.setZipCode("63131");
        savedUser.setEmail("pravin_pachbhai@yahoo.com");

        Mockito.when(userRepository.findByEmail("pravin_pachbhai@yahoo.com"))
                .thenReturn(Mono.empty()); // Email doesn't exist yet

        Mockito.when(userRepository.save(Mockito.any(Customer.class)))
                .thenReturn(Mono.just(savedUser));

        webTestClient.post()
                .uri("/api/users")
                .bodyValue(newUser)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Customer.class)
                .value(user -> {
                    assert Objects.requireNonNull(user).getName().equals("Pravin");
                    assert user.getId() != null;
                });
    }

    @Test
    void testUpdateUser() {
        Customer updatedUser = new Customer();
        updatedUser.setId(1);
        updatedUser.setName("Pravin");
        updatedUser.setCity("St Louis");
        updatedUser.setAddress("4567 New Drive");
        updatedUser.setZipCode("63131");
        updatedUser.setEmail("pravin_pachbhai@yahoo.com");
        Customer existingUser = new Customer();
        existingUser.setId(1);
        existingUser.setName("Pravin");
        existingUser.setCity("St Louis");
        existingUser.setAddress("1234 Test Drive");
        existingUser.setZipCode("63131");
        existingUser.setEmail("pravin_pachbhai@yahoo.com");

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Mono.just(existingUser));

        Mockito.when(userRepository.save(Mockito.any(Customer.class)))
                .thenReturn(Mono.just(updatedUser));

        webTestClient.put()
                .uri("/api/users/1")
                .bodyValue(updatedUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class)
                .value(user -> {
                    assert Objects.requireNonNull(user).getName().equals("Pravin");
                });
    }

    @Test
    void testDeleteUser() {
        Customer userToDelete = new Customer();
        userToDelete.setId(1);
        userToDelete.setName("Pravin");
        userToDelete.setCity("St Louis");
        userToDelete.setAddress("1234 Test Drive");
        userToDelete.setZipCode("63131");
        userToDelete.setEmail("pravin_pachbhai@yahoo.com");

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Mono.just(userToDelete));

        Mockito.when(userRepository.delete(Mockito.any(Customer.class)))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/users/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}