package com.example.demo.Model;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Users")

public abstract class User {

    public String id;
    public String Fullname;
    public String Email;
    public String Phone;
    public String Password;
    public String Role;

    public User(String id, String fullname, String email, String phone, String password, String role) {
        this.id = id;
        Fullname = fullname;
        Email = email;
        Phone = phone;
        Password = password;
        Role = role;
    }

    public abstract void login();
    public abstract void createAccount();
    public abstract void print_recipes();
    public abstract void create_recipes();
    public abstract void update_recipes();
    public abstract void delete_recipes();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
