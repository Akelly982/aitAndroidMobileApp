package com.aidankelly.projectmanager.database;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;

import java.io.ByteArrayOutputStream;
import java.sql.Array;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;


public class ProjectDatabaseHelper extends SQLiteOpenHelper {


    private Context context;

    private static final String DATABASE_NAME = "myProjects.dp";
    private static final Integer DATABASE_VERSION = 1;   // change this if you change the database
    private static final String PROJECT_TABLE_NAME = "projects";


    private ByteArrayOutputStream objectByteArrayOutputStream;    // used to convert image
    private byte[] imageInByteArray;
    private Image defaultImage;
    private Context myContext;    // needed for an image view but not used just passed

    //singleton
    private static ProjectDatabaseHelper mInstance = null;



    // Create Table projects CONST
    private static final String COL_ID = "ID";
    private static final String COL_LIST_POS = "LIST_POSITION";
    private static final String COL_PROJECT_NAME = "PROJECT_NAME";
    private static final String COL_TOTAL_PROJECT_COST = "TOTAL_PROJECT_COST";
    private static final String COL_PROJECT_IMAGE = "PROJECT_IMG";




    //SELECT *
    //FROM table
    //ORDER BY column_2 DESC;
    private static final String GET_PROJECT_LIST = "SELECT * FROM " + PROJECT_TABLE_NAME + " ORDER BY " + COL_LIST_POS + " ASC;";
    private static final String GET_PROJECT_LISTPOSITION = "SELECT " + COL_ID + " " + COL_LIST_POS +" FROM " + PROJECT_TABLE_NAME + ";";


    // do not forget you spaces "CREATE TABLE "
    private static final String CREATE_TABLE_ST = "CREATE TABLE " +  PROJECT_TABLE_NAME + "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_LIST_POS + " INTEGER DEFAULT 1, " +
            COL_PROJECT_NAME + " TEXT, " +
            COL_TOTAL_PROJECT_COST + " REAL DEFAULT 0.0, " +
            COL_PROJECT_IMAGE + " BLOB )";     // BLOB is for Binary large data


    // if need something to delete whole table         // IF EXISTS helps avoid errors deleting noting.
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + PROJECT_TABLE_NAME;


    public static synchronized ProjectDatabaseHelper getInstance(Context ctx){
        if (mInstance == null){
            mInstance = new ProjectDatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public ProjectDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override   // if table == null
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ST);
    }

    @Override     // runs if version change
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }





    public ArrayList projectInsert(String projectName,Bitmap projectImage){
        ArrayList<Long> foundErrors = new ArrayList<Long>();

        //List pos will = 1 as Default putting it at top of list
        // update all rows listPos to make room for new listPos prior to running this method
        foundErrors.add(incrementAllProjectsListPosition());

        // add to database
        foundErrors.add(projectInsertDataBase(projectName,projectImage));
        return foundErrors;
    }




        //List pos will = 1 as Default putting it at top of list
        // update all rows listPos to make room for new listPos prior to running this method
    public long projectInsertDataBase( String projectName, Bitmap projectImage){

        // add to database
        //create an instance of SQLite database
                                // this. because referring to this class
        SQLiteDatabase db = this.getWritableDatabase();


        // Like and intent setting up for content transfer
                        //(databaseColumn , data)
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PROJECT_NAME, projectName);
        contentValues.put(COL_PROJECT_IMAGE, byteArrayImageConvert(projectImage));  // converts here to byte array

        // if -1 error else should be a positive num
        Long result = db.insert(PROJECT_TABLE_NAME,null,contentValues);
        //close your database
        db.close();
        return result;
    }




    // increment list Order by +1 so that new project comes in first
    public long incrementAllProjectsListPosition(){
        SQLiteDatabase db = this.getWritableDatabase();
        //GET_ALL_ST -----  SQL search query
        Cursor cursor = db.rawQuery(GET_PROJECT_LISTPOSITION, null);

        long numOfErrors = 0L;

        if (cursor.getCount() > 0){     // no data found
            UserProject project;
            while(cursor.moveToNext()){      // if their is no next row it returns false


                ContentValues contentValues = new ContentValues();

                Integer currentId = cursor.getInt(0);
                Integer listPositionIncrement = cursor.getInt(1) + 1;

                contentValues.put(COL_LIST_POS,listPositionIncrement);

                //set new value to db
                int numOfRowsUpdated = db.update(PROJECT_TABLE_NAME, contentValues, "ID = ?", new String[]{currentId.toString()});
                db.close();
                if (numOfRowsUpdated != 1){  // should only be 1
                    numOfErrors++;
                }

            }
        }

        cursor.close();
        return numOfErrors;
    };





    private Bitmap findADefaultImage() {
        //ImageView myImageView = new ImageView(myContext));      //  myContext declared above but unused  (image View wants one)
        //myImageView.setImageResource(R.drawable.project_default_image);
        //BitmapDrawable drawable = (BitmapDrawable) myImageView.getDrawable();
        BitmapDrawable drawable = ((BitmapDrawable) myContext.getDrawable(R.drawable.project_default_image));   // works with sdk update to 21   // myContext declared above to access context
        Bitmap myNewBitmap = drawable.getBitmap();
        return myNewBitmap;
    }


         // convert bitmap to byte array
    private byte[] byteArrayImageConvert(Bitmap currentBitmap) {
        // convert bitmap image
        currentBitmap.compress(Bitmap.CompressFormat.PNG, 100, objectByteArrayOutputStream);
        imageInByteArray = objectByteArrayOutputStream.toByteArray();
        return imageInByteArray;
    }



         // update whole project
    public boolean projectUpdate (Integer id, Integer listPosition, String projectName, Float totalProjectCost, Bitmap projectImage){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, id);
        contentValues.put(COL_LIST_POS, listPosition);
        contentValues.put(COL_PROJECT_NAME, projectName);
        contentValues.put(COL_TOTAL_PROJECT_COST, totalProjectCost);
        contentValues.put(COL_PROJECT_IMAGE, byteArrayImageConvert(projectImage));

        int numOfRowsUpdated = db.update(PROJECT_TABLE_NAME, contentValues, "ID = ?", new String[]{id.toString()});
        db.close();
        return numOfRowsUpdated == 1;
    }


        // only update projectName
    public boolean projectUpdateName (Integer id, String projectName){
        SQLiteDatabase db = this.getWritableDatabase();

        // Update needs ID to work basic stuff
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, id);
        contentValues.put(COL_PROJECT_NAME, projectName);

        int numOfRowsUpdated = db.update(PROJECT_TABLE_NAME, contentValues, "ID = ?", new String[]{id.toString()});
        db.close();
        return numOfRowsUpdated == 1;
    }

         // only update project image
    public boolean projectUpdateImage (Integer id, Bitmap projectImage){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, id);
        contentValues.put(COL_PROJECT_IMAGE, byteArrayImageConvert(projectImage));

        int numOfRowsUpdated = db.update(PROJECT_TABLE_NAME, contentValues, "ID = ?", new String[]{id.toString()});
        db.close();
        return numOfRowsUpdated == 1;
    }

        // TODO implement list arrangement for Projects
        // update list pos  --  keep in mind <other projects list pos> <the list reads ASC due to  >
    public boolean projectUpdateListPosition(){
        return false;
    }

    public boolean projectDelete (Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        int numOfRowsDeleted = db.delete(PROJECT_TABLE_NAME, "ID = ?", new String[]{id.toString()});
        return numOfRowsDeleted == 1;
    }



    public List<UserProject> getProjects(){
        List<UserProject> projects = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase(); // not as heavy as writable    <---- use when can
                                 //GET_ALL_ST -----  SQL search query
        Cursor cursor = db.rawQuery(GET_PROJECT_LIST, null);


        if (cursor.getCount() > 0){
            UserProject project;
            while(cursor.moveToNext()){      // if their is no next row it returns false

                // keep this in the same order as your columns

                Integer id = cursor.getInt(0);
                Integer listPosition = cursor.getInt(1) ;
                String projectName = cursor.getString(2);
                Float totalProjectCost = cursor.getFloat(3);
                byte[] imageBytes = cursor.getBlob(4);       // Grab blob with byte array

                //convert bytes back to bitmap
                Bitmap projectImage = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);

                project = new UserProject(id,listPosition,projectName,totalProjectCost, projectImage);
                projects.add(project);

            }
        }

        cursor.close();
        return projects;
    };






}
