package com.example.admin.task_assistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactDetails {

    @SerializedName("mobile_no")

    @Expose
    String mobile_no;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("tag")
    @Expose
    String tag;

    @SerializedName("USER_PHOTO")
    @Expose
    String USER_PHOTO;


    public ContactDetails(String mobile_no, String name, String tag, String USER_PHOTO) {
        this.mobile_no = mobile_no;
        this.name = name;
        this.tag = tag;
        this.USER_PHOTO = USER_PHOTO;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUSER_PHOTO() {
        return USER_PHOTO;
    }

    public void setUSER_PHOTO(String USER_PHOTO) {
        this.USER_PHOTO = USER_PHOTO;
    }
}
