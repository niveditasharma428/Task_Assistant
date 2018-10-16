package com.example.admin.task_assistant;

public class User {

    private String mobile, email, gender;

    public User(String mobile, String email) {
        this.mobile = mobile;
        this.email = email;
        // this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    // public String getGender() {
    // return gender;
    //}
}



