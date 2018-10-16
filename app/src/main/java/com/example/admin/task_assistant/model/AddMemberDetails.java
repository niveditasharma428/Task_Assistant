package com.example.admin.task_assistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddMemberDetails {

    @SerializedName("mobile_no")
    @Expose
    String mobile_no;

    @SerializedName("name")
    @Expose
    String name;

    public AddMemberDetails(String mobile_no, String name) {
        this.mobile_no = mobile_no;
        this.name = name;
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
}
