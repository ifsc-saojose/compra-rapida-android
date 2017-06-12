package com.example.beatriz.listatcc.Model;

public class User {

    public String userName;
    public String email;
    public String photo;

    public User() {

    }

    public User(String userName, String email, String photo) {
        this.userName = userName;
        this.email = email;
        this.photo = photo;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }


}
