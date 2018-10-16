package com.example.admin.task_assistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyGroupMembersList {

    @SerializedName("GROUP_MEMBER_NAME")
    @Expose
    String GROUP_MEMBER_NAME;

    @SerializedName("GROUP_MEMBER_MOBILE")
    @Expose
    String GROUP_MEMBER_MOBILE;

    public String getGROUP_MEMBER_NAME() {
        return GROUP_MEMBER_NAME;
    }

    public MyGroupMembersList(String GROUP_MEMBER_NAME, String GROUP_MEMBER_MOBILE) {
        this.GROUP_MEMBER_NAME = GROUP_MEMBER_NAME;
        this.GROUP_MEMBER_MOBILE = GROUP_MEMBER_MOBILE;
    }

    public void setGROUP_MEMBER_NAME(String GROUP_MEMBER_NAME) {
        this.GROUP_MEMBER_NAME = GROUP_MEMBER_NAME;
    }

    public String getGROUP_MEMBER_MOBILE() {
        return GROUP_MEMBER_MOBILE;
    }

    public void setGROUP_MEMBER_MOBILE(String GROUP_MEMBER_MOBILE) {
        this.GROUP_MEMBER_MOBILE = GROUP_MEMBER_MOBILE;
    }
}
