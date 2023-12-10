package com.taskmanagement.entity;

import com.taskmanagement.entity.enums.UserRole;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String login;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    UserRole role;

}
