package com.user.search.controller;
import com.user.search.entity.UserEntity;
import com.user.search.exception.UserNotFoundException;
import com.user.search.request.*;
import com.user.search.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserSearchControllerTest {

    private UserSearchController userSearchController;
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = mock(UserService.class);
        userSearchController = new UserSearchController(userService);
    }

    @Test
    public void testRetrieveAllUsers() {
        // Arrange
        List<UserEntity> users = new ArrayList<>();
        UserEntity userEntity = new UserEntity("user1");
        UserEntity userEntity1 = new UserEntity("user2");
        users.add(userEntity);
        users.add(userEntity1);
        when(userService.findAll()).thenReturn(users);
        // Act
        List<UserEntity> result = userSearchController.retrieveAllUsers();
        // Assert
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
    }

    @Test
    public void testRetrieveUser_UserFound() {
        // Arrange
        UserEntity user = new UserEntity("testUser");
        when(userService.findUser(Mockito.anyString())).thenReturn(user);
        // Act
        UserEntity result = userSearchController.retrieveUser("testUser");
        // Assert
        assertEquals("testUser", result.getUsername());
    }

    @Test
    public void testRetrieveUser_UserNotFound() {
        // Arrange
        when(userService.findUser(Mockito.anyString())).thenReturn(null);
        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            userSearchController.retrieveUser("nonExistingUser");
        });
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        String username = "testUser";
        // Act
        userSearchController.deleteUser(username);
        // Assert
        Mockito.verify(userService).deleteByUsername(username);
    }

    @Test
    public void testCreateUser() {
        // Arrange
        UserEntity user = new UserEntity("testUser");
        when(userService.save(user)).thenReturn(user);
        // Act
        ResponseEntity<UserEntity> response = userSearchController.createUser(user);
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testCreateUser_InvalidUser() {
        // Arrange
        UserEntity user = new UserEntity(); // Empty user
        // Act
        ResponseEntity<UserEntity> response = userSearchController.createUser(user);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_UserFound() {
        // Arrange
        UserEntity user = new UserEntity("testUser");
        when(userService.findUser(Mockito.anyString())).thenReturn(user);
        // Act
        ResponseEntity<Object> response = userSearchController.updateUser(user, "testUser");
        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        // Arrange
        UserEntity user = new UserEntity("testUser");
        when(userService.findUser(Mockito.anyString())).thenReturn(null);
        // Act
        ResponseEntity<Object> response = userSearchController.updateUser(user, "nonExistingUser");
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testSearch() {
        // Arrange
        String keyword = "test";
        SearchRequest searchRequest = new SearchRequest();
        List<UserEntity> searchResult = new ArrayList<>();
        searchResult.add(new UserEntity("testUser1"));
        searchResult.add(new UserEntity("testUser2"));
        when(userService.search(keyword, searchRequest)).thenReturn(searchResult);

        // Act
        List<UserEntity> result = userSearchController.search(keyword, searchRequest);

        // Assert
        assertEquals(2, result.size());
        assertEquals("testUser1", result.get(0).getUsername());
        assertEquals("testUser2", result.get(1).getUsername());
    }

}