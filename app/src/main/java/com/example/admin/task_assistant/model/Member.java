package com.example.admin.task_assistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Member {

    @SerializedName("success")
    @Expose
    String success;

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("User")
    @Expose
    List<MemberDetails> memberDetails;

    public List<MemberDetails> getMemberDetails() {
        return memberDetails;
    }

    public void setMemberDetails(List<MemberDetails> memberDetails) {
        this.memberDetails = memberDetails;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
