package com.example.admin.task_assistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyTodoDetails {

    @SerializedName("CREATED_BY")
    @Expose
    String createdBY;

    @SerializedName("NO_OF_TASK")
    @Expose
    String noOfTask;

    @SerializedName("TASK_GROUP")
    @Expose
    String taskGroup;

    public MyTodoDetails(String createdBY, String noOfTask, String taskGroup) {
        this.createdBY = createdBY;
        this.noOfTask = noOfTask;
        this.taskGroup = taskGroup;
    }

    public String getCreatedBY() {

        return createdBY;
    }

    public void setCreatedBY(String createdBY) {
        this.createdBY = createdBY;
    }

    public String getNoOfTask() {
        return noOfTask;
    }

    public void setNoOfTask(String noOfTask) {
        this.noOfTask = noOfTask;
    }

    public String getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }
}
