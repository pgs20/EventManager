package dev.petrov.dto.usersDto;

public class UserDto {
    private Integer id;
    private String login;
    private Integer age;
    private UserRole userRole;

    public UserDto(Integer id, String login, Integer age, UserRole userRole) {
        this.id = id;
        this.login = login;
        this.age = age;
        this.userRole = userRole;
    }

    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public Integer getAge() {
        return age;
    }

    public UserRole getRole() {
        return userRole;
    }
}
