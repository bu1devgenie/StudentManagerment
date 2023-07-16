package com.app.studentManagerment.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "account")

public class Account {
    @Id
    @Column(name = "email", nullable = false, length = 50)
    private String email;
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_role", joinColumns = @JoinColumn(name = "email"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> role;

    public Account() {
    }


    public Account(String email, String password, List<Role> role) {
        this.email = email;
        this.password = password;
        this.role = role;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Account{" +
               "email='" + email + '\'' +
               ", password='" + password + '\'' +
               ", role=" + role +
               '}';
    }
}
