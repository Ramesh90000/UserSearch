package com.user.search.service;

import com.user.search.entity.*;
import com.user.search.repository.*;
import com.user.search.request.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public UserService(UserRepository userRepository, EntityManager entityManager){
        this.userRepository = userRepository;
        this.entityManager = entityManager;
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

    public List<UserEntity> search(String keyword, SearchRequest searchRequest) {

        List<String> columns;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> q = cb.createQuery(UserEntity.class);
        Root<UserEntity> userEntityRoot = q.from(UserEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        columns = searchRequest.getColumns();

        for (int i = 0; i < columns.size(); i++) {
            predicates.add(cb.or(cb.like(userEntityRoot.get(String.valueOf(columns.get(i))).as(String.class), "%" + keyword + "%")));
        }
        q.select(userEntityRoot).where(
                cb.or(predicates.toArray(new Predicate[predicates.size()])));
        List<UserEntity> resultList = entityManager.createQuery(q).getResultList();
        return resultList;
    }
}
