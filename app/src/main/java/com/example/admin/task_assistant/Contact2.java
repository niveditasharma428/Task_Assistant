package com.example.admin.task_assistant;

public class Contact2 {


    public String TASK_TITLE;
    public String TASK_ID;
    public  String TASK_PRIORITY;
    public  String TASK_COMMENT;
    public  String TASK_STATUS;
    public  String ASSIGN_TO;



    public Contact2(String title,String priority,String status,String assign, String task_id,String task_comment){
        this.TASK_TITLE=title;
        this.TASK_ID=task_id;
        this.ASSIGN_TO=assign;
        this.TASK_PRIORITY=priority;
        this.TASK_STATUS=status;
        this.TASK_COMMENT=task_comment;

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



    public String getAssign() {

        return ASSIGN_TO;
    }

    public void setAssign(String userassign) {

        this.ASSIGN_TO = userassign;
    }


    public String getComment() {

        return TASK_COMMENT;
    }

    public void setComment(String usercomment) {

        this.TASK_COMMENT = usercomment;
    }



    public String getTASK_STATUS() {

        return TASK_STATUS;
    }

    public void setTASK_STATUS(String taskStatus) {

        this.TASK_STATUS = taskStatus;
    }

    public String getTASK_PRIORITY() {

        return TASK_PRIORITY;
    }

    public void setTASK_PRIORITY(String taskpriority) {

        this.TASK_PRIORITY = taskpriority;
    }

}
