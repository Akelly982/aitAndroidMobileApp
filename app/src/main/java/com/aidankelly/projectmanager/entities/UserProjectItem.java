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
    private byte[] ImageBytes;
    private Integer foreignKey;


    public UserProjectItem() {

    }

    public UserProjectItem(Integer id, Integer listPos, String description, Float cost, Bitmap imageBitmap, Integer foreignKey) {
        this.id = id;
        this.listPos = listPos;
        this.description = description;
        this.cost = cost;
        this.ImageBytes = byteArrayImageConvert(imageBitmap);
        this.foreignKey = foreignKey;
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

        return imageInByteArray; // return byte[]
    }

    //convert bytes back to bitmap
    public Bitmap imageConverterFromArray(byte[] myImageByteArray){
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(myImageByteArray,0,myImageByteArray.length);
        return bitmapImage;
    }


    //byte[] getter and setters

    // acts like working with bitmap but changing between Bitmap and byte array on get and set
    // working with byte[] getters and setters for image
    public Bitmap getImage() {
        return imageConverterFromArray(ImageBytes);
    }

    public void setImage(Bitmap bitImage) {
        this.ImageBytes = byteArrayImageConvert(bitImage);
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
