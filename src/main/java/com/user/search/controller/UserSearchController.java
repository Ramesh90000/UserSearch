package com.user.search.controller;

import com.user.search.entity.*;
import com.user.search.exception.*;
import com.user.search.request.*;
import com.user.search.service.*;
import org.springframework.http.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.*;

import java.net.*;
import java.util.*;

@RestController
public class UserSearchController {

    private UserService service;

    public UserSearchController(UserService service) {
        this.service = service;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<UserEntity> search(@RequestParam String keyword, @RequestBody(required = false) SearchRequest searchRequest) {
        return service.search(keyword, searchRequest);
    }

    @GetMapping("/users")
    public List<UserEntity> retrieveAllUsers() {
        return service.findAll();
    }

    @GetMapping("/users/{username}")
    public UserEntity retrieveUser(@PathVariable String username) {
        UserEntity user = service.findUser(username);

        if(user==null)
            throw new UserNotFoundException("username: "+username);

        return user;
    }

    @DeleteMapping("/users/{username}")
    public void deleteUser(@PathVariable String username) {
        service.deleteByUsername(username);
    }

    @PostMapping("/users")
    public ResponseEntity<UserEntity> createUser(@Validated @RequestBody UserEntity user) {

        UserEntity savedUser = service.save(user);

        if (savedUser != null){
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedUser.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/users/{username}")
    public ResponseEntity<Object> updateUser(@RequestBody UserEntity userEntity, @PathVariable String username) {
        UserEntity user = service.findUser(username);
        if (user == null)
            return ResponseEntity.notFound().build();
        service.update(user);
        return ResponseEntity.noContent()
                .build();
    }
}
