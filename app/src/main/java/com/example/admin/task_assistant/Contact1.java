package com.example.admin.task_assistant;

public class Contact1 {


    public String TASK_TITLE;
    public String TASK_ID;
    public  String TASK_PRIORITY;
    public  String STATUS;
    public  String TASK_COMMENT;
    public  String CREATED_BY;

    public Contact1(String title,String priority,String TASK_ID,String CREATED_BY,String status,String comment){
        this.TASK_TITLE=title;


        this.TASK_PRIORITY=priority;
        this.TASK_ID=TASK_ID;
        this.CREATED_BY=CREATED_BY;
        this.STATUS=status;
        this.TASK_COMMENT=comment;


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

    public String getCREATED_BY() {

        return CREATED_BY;
    }

    public void setCREATED_BY(String created_by) {

        this.CREATED_BY = created_by;
    }

    public String getSTATUS() {

        return STATUS;
    }

    public void setSTATUS(String status) {

        this.STATUS = status;
    }
    public String getCOMMENT() {

        return TASK_COMMENT;
    }

    public void setCOMMENT(String comment) {

        this.TASK_COMMENT = comment;
    }

    public String getTASK_PRIORITY() {

        return TASK_PRIORITY;
    }

    public void setTASK_PRIORITY(String taskpriority) {

        this.TASK_PRIORITY = taskpriority;
    }


}
