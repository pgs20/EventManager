package dev.petrov.entity;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_login", unique = true)
    private String login;
    @Column(name = "user_password", nullable = false)
    private String password;
    @Column(name = "user_age")
    private Integer age;
    @Column(name = "role")
    private String role;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<RegistrationEntity> registrationEntities;

    public UserEntity() {
    }

    public UserEntity(String login, String password, String userRole) {
        this.login = login;
        this.password = password;
        this.role = userRole;
    }

    public UserEntity(String login, String password, Integer age, String userRole) {
        this.login = login;
        this.password = password;
        this.age = age;
        this.role = userRole;
    }

    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", passwordHash='" + password + '\'' +
                ", age=" + age +
                ", role='" + role + '\'' +
                '}';
    }

    public List<RegistrationEntity> getRegistrationEntities() {
        return registrationEntities;
    }
}
