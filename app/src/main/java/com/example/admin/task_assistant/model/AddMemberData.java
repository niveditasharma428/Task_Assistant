package com.example.admin.task_assistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddMemberData {

    @SerializedName("success")
    @Expose
    String success;


    @SerializedName("User")
    @Expose
    List<AddMemberDetails> addMemberDetails;

    public String getSuccess() {
        return success;
    }


    public void setSuccess(String success) {
        this.success = success;
    }

    public List<AddMemberDetails> getAddMemberDetails() {
        return addMemberDetails;
    }

    public void setAddMemberDetails(List<AddMemberDetails> addMemberDetails) {
        this.addMemberDetails = addMemberDetails;
    }
}
