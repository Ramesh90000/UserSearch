package com.user.search.service;

import com.user.search.entity.UserEntity;
import com.user.search.repository.UserRepository;
import com.user.search.request.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private EntityManager entityManager;

    @BeforeEach
    public void setup() {
        userRepository = mock(UserRepository.class);
        entityManager = mock(EntityManager.class);
        userService = new UserService(userRepository, entityManager);
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
    public void testSearch() {
        // Arrange
        String keyword = "test";
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setColumns(List.of("username", "firstName"));
        List<UserEntity> expectedResults = new ArrayList<>();
        expectedResults.add(new UserEntity("testUser1"));
        expectedResults.add(new UserEntity("testUser2"));
        when(entityManager.getCriteriaBuilder()).thenReturn(mock(CriteriaBuilder.class));
        when(entityManager.createQuery(Mockito.any(CriteriaQuery.class)).getResultList()).thenReturn(expectedResults);

        // Act
        List<UserEntity> result = userService.search(keyword, searchRequest);

        // Assert
        assertEquals(2, result.size());
        assertEquals("testUser1", result.get(0).getUsername());
        assertEquals("testUser2", result.get(1).getUsername());
    }
}
