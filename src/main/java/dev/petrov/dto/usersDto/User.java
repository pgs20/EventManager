package dev.petrov.dto.usersDto;

public class User {
    private Integer id;
    private String login;
    private String password;
    private Integer age;
    private UserRole userRole;

    public User(String login, String password, Integer age) {
        this.login = login;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    public UserRole getRole() {
        return userRole;
    }
}
