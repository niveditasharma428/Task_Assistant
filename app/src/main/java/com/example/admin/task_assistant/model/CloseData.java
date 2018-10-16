package com.example.admin.task_assistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CloseData {
    @SerializedName("success")
    @Expose
    String success;

    @SerializedName("User1")
    @Expose
    List<CloseTaskDetails> closeUser1;

    @SerializedName("User2")
    @Expose
    List<CloseTaskDetails> closeUser2;

    public CloseData(String success, List<CloseTaskDetails> closeUser1, List<CloseTaskDetails> closeUser2) {
        this.success = success;
        this.closeUser1 = closeUser1;
        this.closeUser2 = closeUser2;
    }

    public String getSuccess() {

        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<CloseTaskDetails> getCloseUser1() {
        return closeUser1;
    }

    public void setCloseUser1(List<CloseTaskDetails> closeUser1) {
        this.closeUser1 = closeUser1;
    }

    public List<CloseTaskDetails> getCloseUser2() {
        return closeUser2;
    }

    public void setCloseUser2(List<CloseTaskDetails> closeUser2) {
        this.closeUser2 = closeUser2;
    }
}
