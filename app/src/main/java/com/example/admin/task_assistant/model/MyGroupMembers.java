package com.example.admin.task_assistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyGroupMembers
{
    @SerializedName("success")
    @Expose
    String success;

    @SerializedName("User")
    @Expose
    List<MyGroupMembersList> myGroupMembersLists;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<MyGroupMembersList> getMyGroupMembersLists() {
        return myGroupMembersLists;
    }

    public void setMyGroupMembersLists(List<MyGroupMembersList> myGroupMembersLists) {
        this.myGroupMembersLists = myGroupMembersLists;
    }
}
