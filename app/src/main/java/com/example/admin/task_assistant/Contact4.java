package com.example.admin.task_assistant;

public class Contact4 {

    public String Name;
    public String TaskAssign;
    public String Task;

    public Contact4(String name, String task) {
        this.Name = name;
        this.Task = task;

    }

    public String getName() {

        return Name;
    }

    public void setName(String username) {

        this.Name = username;
    }

    public String getTaskCount() {

        return Task;
    }

    public void setTaskCount(String usercount) {

        this.Task = usercount;
    }
}
