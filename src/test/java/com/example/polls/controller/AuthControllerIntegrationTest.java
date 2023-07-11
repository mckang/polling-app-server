package com.example.polls.controller;

import java.util.Arrays;

import javax.persistence.EntityManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.polls.model.User;
import com.example.polls.payload.ApiResponse;
import com.example.polls.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerIntegrationTest {

    @LocalServerPort
    private int localServerPort;

    @Autowired
    private TestRestTemplate testRestTemplate;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EntityManager entityManager;

    private String authorizationToken;
    
    @Test
    @DisplayName("User can be created")
    @Order(1)
    void testCreateUser_whenValidDetailsProvided_returnsUserDetails() throws JSONException {
        // Arrange

        JSONObject userDetailsRequestJson = new JSONObject();
        userDetailsRequestJson.put("name", "Sergey");
        userDetailsRequestJson.put("username", "test2@test.com");
        userDetailsRequestJson.put("email", "test2@test.com");
        userDetailsRequestJson.put("password","12345678");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(userDetailsRequestJson.toString(), headers);

        // Act
        ResponseEntity<ApiResponse> createdUserDetailsEntity = testRestTemplate.postForEntity("/api/auth/signup",
                request,
                ApiResponse.class);
        ApiResponse apiResponse = createdUserDetailsEntity.getBody();

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, createdUserDetailsEntity.getStatusCode());
        
        User user = userRepository.findByEmail("test2@test.com").get();
        
        userRepository.delete(user);

    }    
    
}
