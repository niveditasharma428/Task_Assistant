package com.example.admin.task_assistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyGroups {

    @SerializedName("success")
    @Expose
    String success;

    @SerializedName("User")
    @Expose
    List<MyGroupMemberDetails> myGroupMemberDetails;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<MyGroupMemberDetails> getMyGroupMemberDetails() {
        return myGroupMemberDetails;
    }

    public void setMyGroupMemberDetails(List<MyGroupMemberDetails> myGroupMemberDetails) {
        this.myGroupMemberDetails = myGroupMemberDetails;
    }
}
