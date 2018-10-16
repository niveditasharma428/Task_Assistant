package com.example.admin.task_assistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CloseTaskDetails {

    @SerializedName("TASK_ID")
    @Expose
    String TASK_ID;
    @SerializedName("TASK_DES")
    @Expose
    String TASK_DES;
    @SerializedName("TASK_ASSIGN")
    @Expose
    String TASK_ASSIGN;
    @SerializedName("TASK_PRIORITY")
    @Expose
    String TASK_PRIORITY;
    @SerializedName("TASK_GROUP")
    @Expose
    String TASK_GROUP;



    public CloseTaskDetails(String TASK_DES, String TASK_ASSIGN, String TASK_PRIORITY, String TASK_GROUP, String TASK_ID) {
        this.TASK_DES = TASK_DES;
        this.TASK_ASSIGN = TASK_ASSIGN;
        this.TASK_PRIORITY = TASK_PRIORITY;
        this.TASK_GROUP = TASK_GROUP;
        this.TASK_ID=TASK_ID;

    }


    public String getTASK_DES() {
        return TASK_DES;
    }

    public void setTASK_DES(String TASK_DES) {
        this.TASK_DES = TASK_DES;
    }

    public String getTASK_ASSIGN() {
        return TASK_ASSIGN;
    }

    public void setTASK_ASSIGN(String TASK_ASSIGN) {
        this.TASK_ASSIGN = TASK_ASSIGN;
    }

    public String getTASK_PRIORITY() {
        return TASK_PRIORITY;
    }

    public void setTASK_PRIORITY(String TASK_PRIORITY) {
        this.TASK_PRIORITY = TASK_PRIORITY;
    }

    public String getTASK_GROUP() {
        return TASK_GROUP;
    }

    public void setTASK_GROUP(String TASK_GROUP) {
        this.TASK_GROUP = TASK_GROUP;
    }
    public String getTASK_ID() {
        return TASK_ID;
    }

    public void setTASK_ID(String TASK_ID) {
        this.TASK_ID = TASK_ID;
    }
}
