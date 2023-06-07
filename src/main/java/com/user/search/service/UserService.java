package com.user.search.service;

import com.user.search.entity.*;
import com.user.search.repository.*;
import com.user.search.request.*;
import com.user.search.util.*;
import jakarta.transaction.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    public UserEntity findUser(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteByUsername(String username) {
        userRepository.deleteUserEntityByUsername(username);
    }

    public UserEntity update(UserEntity user){
        UserEntity userEntity = new UserEntity();
        if(StringUtils.isNotBlank(user.getUsername())){
            userEntity.setUsername(user.getUsername());
        }
        if(StringUtils.isNotBlank(user.getFirstName())){
            userEntity.setFirstName(user.getFirstName());
        }
        if(StringUtils.isNotBlank(user.getLastName())){
            userEntity.setLastName(user.getLastName());
        }
        if(StringUtils.isNotBlank(user.getEmail())){
            userEntity.setEmail(user.getEmail());
        }
        if(StringUtils.isNotBlank(user.getAge())){
            userEntity.setAge(user.getAge());
        }
        return userRepository.save(userEntity);
    }

    public List<UserEntity> search(Optional<SearchRequest> input) {

        var searchRequest = input.orElse(new SearchRequest());
        var specification = Specification.where(
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
        return userRepository.findAll(specification);
    }
}
