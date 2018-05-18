package com.example.avish.spm.Adapter;

/**
 * Created by Avish on 08-05-2017.
 */

public class ProjectRow {
    private String title, description, startDate, endDate, projectType;
    private int projectID;


    public ProjectRow(String title, String description, String startDate, String endDate, String projectType, int projectID) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectType = projectType;
        this.projectID = projectID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getProjectType() {
        return projectType;
    }

    public int getProjectID() {
        return projectID;
    }
}
