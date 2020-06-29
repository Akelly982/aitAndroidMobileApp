package com.aidankelly.projectmanager.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;




/*
  forum from which this class
  https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
  Created by Ilya Gazman on 3/6/2016.
 */


public class ImageManager {

    private String directoryName = "testImages";
    private String fileName = "testImage.png";
    private Context context;




    public ImageManager(Context context){
        this.context = context;
    }



    public ImageManager setFileName(String fileName){
        this.fileName = fileName;
        return this;
    }


    public ImageManager setDirectoryName (String directoryName){
        this.directoryName = directoryName;
        return this;
    }


    public ImageManager setDirNameAndFileName(String directoryName, String fileName){
        this.directoryName = directoryName;
        this.fileName = fileName;
        return this;
    }



    public void save(Bitmap bitmapImage){
        FileOutputStream fileOutputStream = null;
        try{
                                               // createFile() fills in the File directory and file name
            fileOutputStream = new FileOutputStream(createFile());
            bitmapImage.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);   // image for save under directory and name
        } catch (Exception e){               // the rest is check results and close
            e.printStackTrace();
        } finally{
            try{
                if (fileOutputStream != null){
                    fileOutputStream.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private File createFile() {
        File directory;
        directory = context.getDir(directoryName, Context.MODE_PRIVATE);

        if (!directory.exists() && !directory.mkdir()){
            Log.e("ImageManager", "Error creating directory" + directory);
        }

        return new File(directory, fileName);
    }


    public Bitmap load() {
        FileInputStream fileInputStream = null;
        Bitmap loadedBitmap = null;
        try {
            fileInputStream = new FileInputStream(createFile());
            loadedBitmap = BitmapFactory.decodeStream(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return loadedBitmap;


    }




    public boolean deleteImageFileUserProject(UserProject myProject){
        File fileDirectory;
        fileDirectory = context.getDir(myProject.getProjectDirectory(), Context.MODE_PRIVATE);

        File fileForDelete = new File(fileDirectory, myProject.getHomeImagePathName());
        boolean result = fileForDelete.delete();
        return result;
    }

    public boolean deleteImageFileProjectItem(UserProject parentProject, UserProjectItem projectItem){
        File fileDirectory;
        fileDirectory = context.getDir(parentProject.getProjectDirectory(), Context.MODE_PRIVATE);

        File fileForDelete = new File(fileDirectory, projectItem.getImagePath());
        boolean result = fileForDelete.delete();
        return result;
    }


    public boolean deleteProjectDirectory(UserProject myProject){
        File directoryForDelete;
        directoryForDelete = context.getDir(myProject.getProjectDirectory(), Context.MODE_PRIVATE);

        // cant delete unless directory is empty
        Boolean result = deleteDirectoryHelper(directoryForDelete); // this deletes directory plus all children files

        return result;


    }


            // loops on its self until all
    public boolean deleteDirectoryHelper(File directoryToBeDeleted){
       File[] directoryAllContents = directoryToBeDeleted.listFiles();
       if (directoryAllContents != null){
           for (File file : directoryAllContents){
               file.delete();

               // could be this if u want it to delete directories within the directory      // above should be fine for me
               //deleteDirectoryHelper(file);
           }
       }
       boolean result = directoryToBeDeleted.delete();
       return result;

    }



}
