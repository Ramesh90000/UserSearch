package com.user.search.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_data")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String age;
    private String email;

    public UserEntity(String username) {
        this.username = username;
    }
}
