package com.example.admin.task_assistant;

public class Contact {

    public String Name;
    public String Phone;
    public String Tag;
    public String Profile;
   // public String id;


    public Contact(String name, String phone,String tag,String profile) {
        this.Name = name;
        this.Phone = phone;
        this.Tag = tag;
        this.Profile = profile;

    }

    public String getName() {

        return Name;
    }

    public void setName(String username) {

        this.Name = username;
    }

    public String getPhone() {

        return Phone;
    }

    public void setPhone(String userphone) {

        this.Phone = userphone;
    }

    public String getTag() {

        return Tag;
    }

    public void setTag(String usertag) {

        this.Tag = usertag;
    }

    public String getImage() {

        return Profile;
    }

    public void setImage(String userprofile) {

        this.Profile = userprofile;
    }
}
