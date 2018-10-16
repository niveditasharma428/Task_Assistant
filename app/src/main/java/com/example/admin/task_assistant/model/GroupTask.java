package com.example.admin.task_assistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupTask {

    @SerializedName("success")
    @Expose
    String success;

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("User")
    @Expose
    List<GroupTaskDetails> groupTaskDetails;

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

    public List<GroupTaskDetails> getGroupTaskDetails() {
        return groupTaskDetails;
    }

    public void setGroupTaskDetails(List<GroupTaskDetails> groupTaskDetails) {
        this.groupTaskDetails = groupTaskDetails;
    }
}
