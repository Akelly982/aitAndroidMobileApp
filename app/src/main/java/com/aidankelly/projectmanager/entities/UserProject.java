package com.aidankelly.projectmanager.entities;

import java.io.Serializable;

public class UserProject implements Serializable {

    public static final String USER_PROJECT_KEY = "userProjectKey"  ; // part of the class
    public static final String USER_PROJECT_KEY_ORIGINAL = "userProjectKeyOriginal"  ;
    public static final String USER_PROJECT_ID_KEY = "userProjectKeyId"  ;
    public static final String USER_PROJECT_PROJECT_NAME = "userProjectKeyName";


    private Integer id;
    private Integer listPosition;
    private String projectName;
    private Float  totalProjectCost;
    private String projectDirectory;      // myImages
    private String homeImagePathName;     // image.png String



    public UserProject() {

    }

    public UserProject(Integer id, Integer listPosition, String projectName, Float totalProjectCost, String projectDirectory, String homeImagePathName) {
        this.id = id;
        this.listPosition = listPosition;
        this.projectName = projectName;
        this.totalProjectCost = totalProjectCost;
        this.projectDirectory = projectDirectory;
        this.homeImagePathName = homeImagePathName;
    }








    // getters and setters for the rest

    public Integer getListPosition() {
        return listPosition;

    }

    public void setListPosition(Integer listPosition) {
        this.listPosition = listPosition;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Float getTotalProjectCost() {
        return totalProjectCost;
    }

    public void setTotalProjectCost(Float totalProjectCost) {
        this.totalProjectCost = totalProjectCost;
    }

    public String getProjectDirectory() {
        return projectDirectory;
    }

    public String getHomeImagePathName() {
        return homeImagePathName;
    }

    public void setProjectDirectory(String projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    public void setHomeImagePathName(String homeImagePathName) {
        this.homeImagePathName = homeImagePathName;
    }






}
