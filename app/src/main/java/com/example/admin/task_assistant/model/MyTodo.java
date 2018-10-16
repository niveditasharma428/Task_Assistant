package com.example.admin.task_assistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyTodo {
    @SerializedName("success")
    @Expose
    String success;

    @SerializedName("User1")
    @Expose
    List<MyTodoDetails> myTodoDetailsListUser1;

    @SerializedName("User2")
    @Expose
    List<MyTodoDetails> myTodoDetailsListUser2;

    public MyTodo(String success, List<MyTodoDetails> myTodoDetailsListUser1, List<MyTodoDetails> myTodoDetailsListUser2) {
        this.success = success;
        this.myTodoDetailsListUser1 = myTodoDetailsListUser1;
        this.myTodoDetailsListUser2 = myTodoDetailsListUser2;
    }

    public String getSuccess() {

        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<MyTodoDetails> getMyTodoDetailsListUser1() {
        return myTodoDetailsListUser1;
    }

    public void setMyTodoDetailsListUser1(List<MyTodoDetails> myTodoDetailsListUser1) {
        this.myTodoDetailsListUser1 = myTodoDetailsListUser1;
    }

    public List<MyTodoDetails> getMyTodoDetailsListUser2() {
        return myTodoDetailsListUser2;
    }

    public void setMyTodoDetailsListUser2(List<MyTodoDetails> myTodoDetailsListUser2) {
        this.myTodoDetailsListUser2 = myTodoDetailsListUser2;
    }

}
