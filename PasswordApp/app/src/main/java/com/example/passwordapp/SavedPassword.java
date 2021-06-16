package com.example.passwordapp;

public class SavedPassword {

    private String title;
    private String username;
    private String password;

    public SavedPassword(String title, String username, String password){
        this.title = title;
        this.username = username;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
