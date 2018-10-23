package com.example.admin.task_assistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemberDetails {

    @SerializedName("GROUP_NAME")
    @Expose
    String GROUP_NAME;

    @SerializedName("GROUP_MEMBER")
    @Expose
    String GROUP_MEMBER;

    @SerializedName("CREATED_DATE")
    @Expose
    String CREATED_DATE;

    @SerializedName("CREATED_BY")
    @Expose
    String CREATED_BY;

    @SerializedName("USER_PHOTO")
    @Expose
    String USER_PHOTO;

    public String getCREATED_DATE() {
        return CREATED_DATE;
    }

    public void setCREATED_DATE(String CREATED_DATE) {
        this.CREATED_DATE = CREATED_DATE;
    }

    public String getGROUP_NAME() {

        return GROUP_NAME;
    }

    public void setGROUP_NAME(String GROUP_NAME) {
        this.GROUP_NAME = GROUP_NAME;
    }

    public String getGROUP_MEMBER() {
        return GROUP_MEMBER;
    }

    public void setGROUP_MEMBER(String GROUP_MEMBER) {
        this.GROUP_MEMBER = GROUP_MEMBER;
    }
    public String getCREATED_BY() {
        return CREATED_BY;
    }

    public void setCREATED_BY(String CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
    }



    public MemberDetails(String GROUP_NAME, String GROUP_MEMBER, String CREATED_DATE, String CREATED_BY) {

        this.GROUP_NAME = GROUP_NAME;
        this.GROUP_MEMBER = GROUP_MEMBER;
        this.CREATED_DATE = CREATED_DATE;
        this.CREATED_BY = CREATED_BY;

    }
}
