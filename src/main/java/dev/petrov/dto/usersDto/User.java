package dev.petrov.dto.usersDto;

public class User {
    private Integer id;
    private String login;
    private String passwordHash;
    private Integer age;
    private UserRole userRole;

    public User(String login, String passwordHash, Integer age) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.age = age;
        this.userRole = UserRole.USER;
    }

    public User(Integer id, String login, Integer age) {
        this.id = id;
        this.login = login;
        this.age = age;
        this.userRole = UserRole.USER;
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

    public UserRole getRole() {
        return userRole;
    }
}
