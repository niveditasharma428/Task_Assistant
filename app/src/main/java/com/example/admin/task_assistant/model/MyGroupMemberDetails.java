package com.example.admin.task_assistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyGroupMemberDetails {
    public String getGROUP_NAME() {
        return GROUP_NAME;
    }

    public void setGROUP_NAME(String GROUP_NAME) {
        this.GROUP_NAME = GROUP_NAME;
    }

    public String getCREATED_BY() {
        return CREATED_BY;
    }

    public void setCREATED_BY(String CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
    }

    @SerializedName("GROUP_NAME")
    @Expose
    String GROUP_NAME;

    public String getCREATED_DATE() {
        return CREATED_DATE;
    }

    public void setCREATED_DATE(String CREATED_DATE) {
        this.CREATED_DATE = CREATED_DATE;
    }

    @SerializedName("CREATED_DATE")
    @Expose
    String CREATED_DATE;

    public MyGroupMemberDetails(String GROUP_NAME, String CREATED_BY, String CREATED_DATE) {
        this.GROUP_NAME = GROUP_NAME;
        this.CREATED_BY = CREATED_BY;
        this.CREATED_DATE=CREATED_DATE;
    }

    @SerializedName("CREATED_BY")
    @Expose
    String CREATED_BY;

}
