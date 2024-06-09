package br.com.picpaychlng.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "name", length = 60, nullable = false)
    private String name;

    @Column(name = "cpf", nullable = false, unique = true,
    length = 11)
    private String cpf;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    public User() {
    }

    public User(String cpf, String email, String name, String password, UserType userType) {
        this.cpf = cpf;
        this.email = email;
        this.name = name;
        this.password = password;
        this.userType = userType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getname(){
        return name;
    }

    public  void setname(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
    return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(cpf, user.cpf) &&
                Objects.equals(email, user.email) &&
                Objects.equals(name, user.name) &&
                Objects.equals(password, user.password) &&
                userType == user.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cpf, email, name, password, userType);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name=" + name + '\'' +
                ", cpf=" + cpf +
                ", email=" + email + '\'' +
                '}';
    }
}

