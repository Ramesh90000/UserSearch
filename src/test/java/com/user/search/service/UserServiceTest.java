package com.user.search.service;

import com.user.search.entity.UserEntity;
import com.user.search.repository.UserRepository;
import com.user.search.request.*;
import com.user.search.util.*;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.*;
import org.springframework.data.jpa.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity("user1"));
        users.add(new UserEntity("user2"));
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserEntity> result = userService.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
    }

    @Test
    public void testSave() {
        // Arrange
        UserEntity user = new UserEntity("testUser");
        when(userRepository.save(user)).thenReturn(user);

        // Act
        UserEntity result = userService.save(user);

        // Assert
        assertEquals("testUser", result.getUsername());
    }

    @Test
    public void testFindUser_UserFound() {
        // Arrange
        UserEntity user = new UserEntity("testUser");
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);

        // Act
        UserEntity result = userService.findUser("testUser");

        // Assert
        assertEquals("testUser", result.getUsername());
    }

    @Test
    public void testFindUser_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);

        // Act
        UserEntity result = userService.findUser("nonExistingUser");

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeleteByUsername() {
        // Arrange
        String username = "testUser";

        // Act
        userService.deleteByUsername(username);

        // Assert
        Mockito.verify(userRepository).deleteUserEntityByUsername(username);
    }

    @Test
    public void testUpdate() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setAge("30");
        when(userRepository.save(Mockito.any())).thenReturn(user);
        // Act
        UserEntity result = userService.update(user);
        // Assert
        assertEquals("testUser", result.getUsername());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("30", result.getAge());
    }

    @Test
    public void testSearch_WithSearchRequest() {
        // Arrange
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setFirstName("John");
        searchRequest.setLastName("Doe");
        searchRequest.setUsername("johndoe");
        Specification<UserEntity> specification = Specification.where(
                StringUtils.isNotBlank(searchRequest.getFirstName()) ?
                        UserSpecification.firstNameContainsIgnoreCase(searchRequest.getFirstName()) :
                        null
        ).and(
                StringUtils.isNotBlank(searchRequest.getLastName()) ?
                        UserSpecification.lastNameContainsIgnoreCase(searchRequest.getLastName()) :
                        null
        ).and(
                StringUtils.isNotBlank(searchRequest.getUsername()) ?
                        UserSpecification.usernameContainsIgnoreCase(searchRequest.getUsername()) :
                        null
        );
        when(userRepository.findAll(specification)).thenReturn(new ArrayList<>());

        // Act
        List<UserEntity> result = userService.search(Optional.of(searchRequest));

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
