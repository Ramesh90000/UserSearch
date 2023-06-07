package com.user.search.repository;

import com.user.search.entity.*;
import com.user.search.request.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>,
        JpaSpecificationExecutor<UserEntity> {
    long deleteUserEntityByUsername(String username);
    public UserEntity findByUsername(String username);
}
