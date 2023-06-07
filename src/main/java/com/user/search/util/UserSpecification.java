package com.user.search.util;

import com.user.search.entity.*;
import org.springframework.data.jpa.domain.*;

public class UserSpecification extends BaseSpecification{

    public static final String FIELD_FIRSTNAME = "firstName";
    public static final String FIELD_LASTNAME = "lastName";
    public static final String FIELD_USERNAME = "username";

    public static Specification<UserEntity> firstNameContainsIgnoreCase(String keyword){
        return (root, query, criteriaBuilder)->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(FIELD_FIRSTNAME)),
                        contains(keyword.toLowerCase())
                );
    }

    public static Specification<UserEntity> lastNameContainsIgnoreCase(String keyword){
        return (root, query, criteriaBuilder)->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(FIELD_LASTNAME)),
                        contains(keyword.toLowerCase())
                );
    }

    public static Specification<UserEntity> usernameContainsIgnoreCase(String keyword){
        return (root, query, criteriaBuilder)->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(FIELD_USERNAME)),
                        contains(keyword.toLowerCase())
                );
    }
}
