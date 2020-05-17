package com.aidankelly.projectmanager.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class UserProject implements Serializable {

    public static final String USER_PROJECT_KEY = "userProjectKey"  ; // part of the class
    public static final String USER_PROJECT_ID_KEY = "userProjectKeyID"  ;
    public static final String USER_PROJECT_PROJECT_NAME = "userProjectKEYName";


    private Integer id;
    private Integer listPosition;
    private String projectName;
    private Float  totalProjectCost;
    //private Bitmap projectImage;      // this does NOT work if SERIALIZED be aware ERRORS
    private byte[] projectImageBytes;



    public UserProject() {

    }

    public UserProject(Integer id, Integer listPosition, String projectName, Float totalProjectCost, Bitmap projectImage) {
        this.id = id;
        this.listPosition = listPosition;
        this.projectName = projectName;
        this.totalProjectCost = totalProjectCost;
        this.projectImageBytes = byteArrayImageConvert(projectImage); // adjust for byte[] conversion
    }






    // convert bitmap to byte array
    private byte[] byteArrayImageConvert(Bitmap currentBitmap) {
        // convert bitmap image

        // holder for return
        byte[] imageInByteArray;

        // take current bitmap and compress
        ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
        currentBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);

        imageInByteArray = objectByteArrayOutputStream.toByteArray();

        return imageInByteArray;
    }


    //convert bytes back to bitmap
    public Bitmap imageConverterFromArray(byte[] myImageByteArray){
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(myImageByteArray,0,myImageByteArray.length);
        return bitmapImage;
    }






    //byte[] getter and setters

    // acts like working with bitmap but changing between Bitmap and byte array on get and set
    // working with byte[] getters and setters for image
    public Bitmap getProjectImage() {
        return imageConverterFromArray(projectImageBytes);
    }

    public void setProjectImage(Bitmap projectImage) {
        this.projectImageBytes = byteArrayImageConvert(projectImage);
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

//    public Bitmap getProjectImage() {
//        return projectImage;
//    }
//
//    public void setProjectImage(Bitmap projectImage) {
//        this.projectImage = projectImage;
//    }




}
