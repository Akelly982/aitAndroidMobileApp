package com.aidankelly.projectmanager.entities;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.Serializable;

public class UserProject implements Serializable {

    public static final String USER_PROJECT_KEY = "build and developed"; // part of the class

    private Integer id;
    private String projectName;
    private Float  totalProjectCost;
    private Bitmap projectImage;

    public UserProject() {
    }

    public UserProject(String projectName, Float totalProjectCost, Bitmap projectImage) {
        this.projectName = projectName;
        this.totalProjectCost = totalProjectCost;
        this.projectImage = projectImage;
    }

    // getters and setters


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

    public Bitmap getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(Bitmap projectImage) {
        this.projectImage = projectImage;
    }
}
