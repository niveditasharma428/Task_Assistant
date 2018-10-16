package com.example.admin.task_assistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupTaskDetails {

    @SerializedName("TASK_ID")
    @Expose
    String TASK_ID;

    @SerializedName("TASK_DES")
    @Expose
    String TASK_DES;

    @SerializedName("TASK_COMMENT")
    @Expose
    String TASK_COMMENT;

    @SerializedName("TASK_PRIORITY")
    @Expose
    String TASK_PRIORITY;

    @SerializedName("TASK_STATUS")
    @Expose
    String TASK_STATUS;

    @SerializedName("TASK_GROUP")
    @Expose
    String TASK_GROUP;

    @SerializedName("TASK_ASSIGN")
    @Expose
    String TASK_ASSIGN;


    public GroupTaskDetails(String TASK_ID, String TASK_DES, String TASK_COMMENT, String TASK_PRIORITY, String TASK_STATUS, String TASK_GROUP, String TASK_ASSIGN) {
        this.TASK_ID = TASK_ID;
        this.TASK_DES = TASK_DES;
        this.TASK_COMMENT = TASK_COMMENT;
        this.TASK_PRIORITY = TASK_PRIORITY;
        this.TASK_STATUS = TASK_STATUS;
        this.TASK_GROUP = TASK_GROUP;
        this.TASK_ASSIGN = TASK_ASSIGN;
    }

    public String getTASK_ID() {

        return TASK_ID;
    }

    public void setTASK_ID(String TASK_ID) {
        this.TASK_ID = TASK_ID;
    }

    public String getTASK_DES() {
        return TASK_DES;
    }

    public void setTASK_DES(String TASK_DES) {
        this.TASK_DES = TASK_DES;
    }

    public String getTASK_COMMENT() {
        return TASK_COMMENT;
    }

    public void setTASK_COMMENT(String TASK_COMMENT) {
        this.TASK_COMMENT = TASK_COMMENT;
    }

    public String getTASK_PRIORITY() {
        return TASK_PRIORITY;
    }

    public void setTASK_PRIORITY(String TASK_PRIORITY) {
        this.TASK_PRIORITY = TASK_PRIORITY;
    }

    public String getTASK_STATUS() {
        return TASK_STATUS;
    }

    public void setTASK_STATUS(String TASK_STATUS) {
        this.TASK_STATUS = TASK_STATUS;
    }

    public String getTASK_GROUP() {
        return TASK_GROUP;
    }

    public void setTASK_GROUP(String TASK_GROUP) {
        this.TASK_GROUP = TASK_GROUP;
    }

    public String getTASK_ASSIGN() {
        return TASK_ASSIGN;
    }

    public void setTASK_ASSIGN(String TASK_ASSIGN) {
        this.TASK_ASSIGN = TASK_ASSIGN;
    }
}
