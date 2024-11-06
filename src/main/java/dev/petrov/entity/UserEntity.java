package dev.petrov.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_login", unique = true)
    private String login;
    @Column(name = "user_password", nullable = false)
    private String passwordHash;
    @Column(name = "user_age")
    private Integer age;
    @Column(name = "role")
    private String role;

    public UserEntity() {
    }

    public UserEntity(String login, String passwordHash, String userRole) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = userRole;
    }

    public UserEntity(String login, String passwordHash, Integer age, String userRole) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.age = age;
        this.role = userRole;
    }

    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
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
                ", passwordHash='" + passwordHash + '\'' +
                ", age=" + age +
                ", role='" + role + '\'' +
                '}';
    }
}
