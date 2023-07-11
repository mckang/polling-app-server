package com.example.polls.repository;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.polls.model.User;

@DataJpaTest(properties = {
		"spring.jpa.hibernate.ddl-auto=create", 
		"spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
		"spring.datasource.initialization-mode=never"})
public class UserRepositoryUnitTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository usersRepository;
    
    private long userId1;
    private long userId2;

    private final String email1 = "test@test.com";
    private final String email2 = "test2@test.com";

    @BeforeEach
    void setup() {
        // Creating first user
    	User userEntity = new User();
//        userEntity.setId(userId1);
        userEntity.setEmail(email1);
        userEntity.setPassword("12345678");
        userEntity.setName("Sergey");
        userEntity.setUsername(email1);
        testEntityManager.persistAndFlush(userEntity);

        userId1 = userEntity.getId();
        
        // Creating second user
        User userEntity2 = new User();
//        userEntity2.setId(userId2);
        userEntity2.setEmail(email2);
        userEntity2.setPassword("abcdefg1");
        userEntity2.setName("John");
        userEntity2.setUsername(email2);
        testEntityManager.persistAndFlush(userEntity2);
        
        userId2 = userEntity2.getId();
    }
    
    
    @Test
    void testFindByEmail_whenGivenCorrectEmail_returnsUserEntity() {
        // Act
    	User storedUser = usersRepository.findByEmail(email1).get();

        // Assert
        Assertions.assertEquals(email1, storedUser.getEmail(),
                "The returned email address does not match the expected value");
    }    
}
