package com.example.avish.spm.Adapter;


public class TaskList {
    String projectname, projectdesscription;


    public TaskList(String projectname, String projectdescription) {

        this.projectname = projectname;
        this.projectdesscription = projectdescription;


    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getProjectdesscription() {
        return projectdesscription;
    }

    public void setProjectdesscription(String projectdesscription) {
        this.projectdesscription = projectdesscription;
    }
}