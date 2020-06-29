package com.aidankelly.projectmanager.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class UserProjectItem implements Serializable {

    public static final String USER_PROJECT_ITEM_KEY = "ProjectItemKEY";

    private Integer id;
    private Integer listPos;
    private String description;
    private Float cost;
    private String imagePath;     // for example myImage.png    << do not forget the .png
    private Integer foreignKey;


    public UserProjectItem() {

    }

    public UserProjectItem(Integer id, Integer listPos, String description, Float cost, String imagePath, Integer foreignKey) {
        this.id = id;
        this.listPos = listPos;
        this.description = description;
        this.cost = cost;
        this.imagePath = imagePath;
        this.foreignKey = foreignKey;
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Integer getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(Integer foreignKey) {
        this.foreignKey = foreignKey;
    }

    public Integer getListPos() {
        return listPos;
    }

    public void setListPos(Integer listPos) {
        this.listPos = listPos;
    }
}
