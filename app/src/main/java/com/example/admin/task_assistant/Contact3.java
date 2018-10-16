package com.example.admin.task_assistant;

public class Contact3 {

    public String TASK_TITLE;
    public String TASK_ID;
    public  String TASK_PRIORITY;
    public  String CREATED_BY;
    public  String ASSIGN_TO;


    public Contact3(String title,String priority, String TASK_ID,String ASSIGN_TO){
        this.TASK_TITLE=title;
        this.TASK_PRIORITY=priority;
        this.TASK_ID=TASK_ID;
        this.ASSIGN_TO=ASSIGN_TO;


    }

    public String getTASK_ID(){
        return TASK_ID;
    }

    public String getTASK_TITLE() {

        return TASK_TITLE;
    }

    public void setTASK_TITLE(String tasktitle) {

        this.TASK_TITLE = tasktitle;
    }

    public String getASSIGN_TO() {

        return ASSIGN_TO;
    }

    public void setASSIGN_TO(String assign_to) {

        this.ASSIGN_TO = assign_to;
    }

    public String getTASK_PRIORITY() {

        return TASK_PRIORITY;
    }

    public void setTASK_PRIORITY(String taskpriority) {

        this.TASK_PRIORITY = taskpriority;
    }



}
