package com.aidankelly.projectmanager.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import androidx.annotation.Nullable;

import com.aidankelly.projectmanager.entities.UserProject;
import com.aidankelly.projectmanager.entities.UserProjectItem;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ProjectDatabaseHelper extends SQLiteOpenHelper {


    private Context context;

    private static final String DATABASE_NAME = "myProjects.db";
    private static final Integer DATABASE_VERSION = 9;   // change this if you change the database
    private static final String PROJECT_TABLE_NAME = "projects";
    private static final String PROJECT_ITEM_TABLE_NAME = "projectItems";


    private ByteArrayOutputStream objectByteArrayOutputStream;    // used to convert image


    //singleton
    private static ProjectDatabaseHelper mInstance = null;



    // Create Table projects CONST
    private static final String COL_PROJECT_ID = "PROJECT_ID";
    private static final String COL_PROJECT_LIST_POS = "PROJECT_LIST_POSITION";
    private static final String COL_PROJECT_NAME = "PROJECT_NAME";
    private static final String COL_PROJECT_TOTAL_COST = "PROJECT_TOTAL_COST";
    private static final String COL_PROJECT_IMAGE = "PROJECT_IMG";

    // Create Table ProjectItems CONST
    private static final String COL_ITEM_ID = "ITEM_ID";
    private static final String COL_ITEM_LIST_POS = "ITEM_LIST_POSITION";
    private static final String COL_ITEM_DESCRIPTION = "ITEM_DESCRIPTION";
    private static final String COL_ITEM_COST = "ITEM_COST";
    private static final String COL_ITEM_IMAGE = "ITEM_IMAGE";
    private static final String COL_ITEM_FOREIGN_KEY = "ITEM_FK";
    private static final String FOREIGN_KEY_CONSTRAINT = "FK_CONSTRAINT";





    //SELECT *
    //FROM table
    //ORDER BY column_2 DESC;
    private static final String GET_PROJECT_LIST = "SELECT * FROM " + PROJECT_TABLE_NAME + " ORDER BY " + COL_PROJECT_LIST_POS + " ASC;";
    private static final String GET_PROJECT_ITEM_LIST = "SELECT * FROM " + PROJECT_ITEM_TABLE_NAME + " WHERE " + COL_ITEM_FOREIGN_KEY + " = ? " + " ORDER BY " + COL_ITEM_LIST_POS + " ASC;";
    private static final String GET_PROJECT_LIST_POSITION = "SELECT " + COL_PROJECT_ID + ", " + COL_PROJECT_LIST_POS +" FROM " + PROJECT_TABLE_NAME + ";";  //needs the id for the update
    private static final String GET_PROJECT_ITEM_LIST_POSITION_WITH_FK = "SELECT " + COL_ITEM_ID + ", " + COL_ITEM_LIST_POS +" FROM " + PROJECT_ITEM_TABLE_NAME + " WHERE " + COL_ITEM_FOREIGN_KEY + "= ?;";
    private static final String GET_PROJECT = "SELECT * FROM " + PROJECT_TABLE_NAME +  " WHERE " + COL_PROJECT_ID + " = ?";
    private static final String GET_PROJECT_ITEM = "SELECT * FROM " + PROJECT_ITEM_TABLE_NAME +  " WHERE " + COL_ITEM_ID + " = ?";
    private static final String GET_PROJECT_BY_LISTPOS_1 = "Select * FROM " + PROJECT_TABLE_NAME + " WHERE " + COL_PROJECT_LIST_POS + " = 1";
    private static final String GET_PROJECT_ITEM_BY_LISTPOS_1 = "Select * FROM " + PROJECT_ITEM_TABLE_NAME + " WHERE " + COL_ITEM_LIST_POS + " = 1";

    // do not forget you spaces "CREATE TABLE "
    private static final String CREATE_TABLE_PROJECT_ST = "CREATE TABLE " +  PROJECT_TABLE_NAME + "(" +
            COL_PROJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_PROJECT_LIST_POS + " INTEGER DEFAULT 1, " +
            COL_PROJECT_NAME + " TEXT, " +
            COL_PROJECT_TOTAL_COST + " REAL DEFAULT 0.0, " +
            COL_PROJECT_IMAGE + " BLOB )";     // BLOB is for Binary large data



    private static final String CREATE_TABLE_PROJECT_ITEM_ST = "CREATE TABLE " +  PROJECT_ITEM_TABLE_NAME + "(" +
            COL_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_ITEM_LIST_POS + " INTEGER DEFAULT 1, " +
            COL_ITEM_DESCRIPTION + " TEXT, " +
            COL_ITEM_COST + " REAL DEFAULT 0.0, " +
            COL_ITEM_IMAGE + " BLOB, " +
            COL_ITEM_FOREIGN_KEY + " INTEGER NOT NULL, " +
            "CONSTRAINT " + FOREIGN_KEY_CONSTRAINT + " " +
            "FOREIGN KEY (" + COL_ITEM_FOREIGN_KEY + ") " +
            "REFERENCES " + PROJECT_TABLE_NAME + " (" + COL_PROJECT_ID + ") " +
            "ON DELETE CASCADE " +
            ")";


    //Drop Table
    // if need something to delete whole table         // IF EXISTS helps avoid errors deleting noting.
    private static final String DROP_TABLE_PROJECT = "DROP TABLE IF EXISTS " + PROJECT_TABLE_NAME ;
    private static final String DROP_TABLE_PROJECT_ITEM = "DROP TABLE IF EXISTS " + PROJECT_ITEM_TABLE_NAME ;


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
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_PROJECT_ST);
        sqLiteDatabase.execSQL(CREATE_TABLE_PROJECT_ITEM_ST);
    }


    @Override     // runs if version change
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // deletes all information in database BAD if you have users
        db.execSQL(DROP_TABLE_PROJECT);
        db.execSQL(DROP_TABLE_PROJECT_ITEM);
        onCreate(db);
    }





    // PROJECT ITEM TABLE METHODS ---------------------------------

    public void itemInsert(String description, Float cost, Bitmap image, Integer foreignKey, ArrayList<Long> foundErrors){
//        ArrayList<Long> foundErrors = new ArrayList<Long>();

        //List pos will = 1 as Default putting it at top of list
        // update all rows listPos to make room for new listPos prior to running this method
        foundErrors.add(incrementAllItemsListPosition(foreignKey));  // foreign key of parent project

        // add to database
        foundErrors.add(itemInsertDataBase(description,cost,image,foreignKey));
    }


     // used on create of new item
    public Long itemInsertDataBase(String description, Float cost, Bitmap image, Integer foreignKey ){   //list pos and id auto set
        SQLiteDatabase db = this.getWritableDatabase();

        byte[] imageInByteArray = byteArrayImageConvert(image);  // converts here to byte array

        // Like and intent setting up for content transfer
        //(databaseColumn , data)
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ITEM_DESCRIPTION, description);
        contentValues.put(COL_ITEM_COST, cost);
        contentValues.put(COL_ITEM_IMAGE, imageInByteArray);
        contentValues.put(COL_ITEM_FOREIGN_KEY, foreignKey);  // should be referring project

        // if -1 error else should be a positive num
        Long result = db.insert(PROJECT_ITEM_TABLE_NAME,null,contentValues);
        //close your database
        db.close();
        return result;
    }

    // increment list Order by +1 so that new item within the project comes in first
    public long incrementAllItemsListPosition(Integer ForeignKey){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(GET_PROJECT_ITEM_LIST_POSITION_WITH_FK,  new String[]{ForeignKey.toString()});

        long numOfErrors = 0L;

        if (cursor.getCount() > 0){     // no data found
            UserProjectItem item;
            while(cursor.moveToNext()){      // if their is no next row it returns false


                ContentValues contentValues = new ContentValues();

                Integer currentId = cursor.getInt(0);
                Integer listPosition = cursor.getInt(1);
                listPosition++;

                contentValues.put(COL_ITEM_LIST_POS,listPosition);

                //set new value to db
                int numOfRowsUpdated = db.update(PROJECT_ITEM_TABLE_NAME, contentValues, COL_ITEM_ID + " = ?", new String[]{currentId.toString()});
                if (numOfRowsUpdated != 1){  // should only be 1
                    numOfErrors++;  // if not 1 their was an error.
                }

            }
        }

        cursor.close();
        db.close();
        return numOfErrors;
    };




    public boolean itemDelete (Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        int numOfRowsDeleted = db.delete(PROJECT_ITEM_TABLE_NAME, COL_ITEM_ID + " = ?", new String[]{id.toString()});
        return  (numOfRowsDeleted == 1);
    }



    public List<UserProjectItem> getItems(Integer ForeignKeyID){
        List<UserProjectItem> items = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();



        Cursor cursor = db.rawQuery(GET_PROJECT_ITEM_LIST,  new String[]{ForeignKeyID.toString()});


        if (cursor.getCount() > 0){
            UserProjectItem item;
            while(cursor.moveToNext()){      // if their is no next row it returns false

                // keep this in the same order as your columns

                Integer id = cursor.getInt(0);
                Integer listPosition = cursor.getInt(1);
                String description = cursor.getString(2);
                Float itemCost = cursor.getFloat(3);
                byte[] imageBytes = cursor.getBlob(4);       // Grab blob with byte array
                Integer foreignKey = cursor.getInt(5);


                //convert bytes back to bitmap
                Bitmap projectImage = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);     // TODO remove double converts of image data

                item = new UserProjectItem(id,listPosition,description,itemCost,projectImage, foreignKey);
                items.add(item);

            }
        }

        db.close();
        cursor.close();
        return items;
    };




    public UserProjectItem getItem(Integer id){
        SQLiteDatabase db = this.getReadableDatabase();

        UserProjectItem item = null;

        // requires item.id followed by foreign key
        Cursor cursor = db.rawQuery(GET_PROJECT_ITEM, new String[]{id.toString()});

        if (cursor.getCount() > 0){
            while(cursor.moveToNext()){


                Integer itemId= cursor.getInt(0);
                Integer listPosition = cursor.getInt(1);
                String description = cursor.getString(2);
                Float cost = cursor.getFloat(3);
                byte[] imageBytes = cursor.getBlob(4);
                Integer foreignKey = cursor.getInt(5);


                //convert bytes back to bitmap
                Bitmap projectImage = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);     // TODO remove double converts of image data

                item = new UserProjectItem(itemId,listPosition,description,cost, projectImage,foreignKey);

            }
        }

        cursor.close();
        db.close();
        return item;
    }

    public UserProjectItem getItemByListPos1(){
        SQLiteDatabase db = this.getReadableDatabase();

        UserProjectItem item = null;

        // requires item.id followed by foreign key
        Cursor cursor = db.rawQuery(GET_PROJECT_ITEM_BY_LISTPOS_1, null);

        if (cursor.getCount() > 0){
            while(cursor.moveToNext()){


                Integer itemId= cursor.getInt(0);
                Integer listPosition = cursor.getInt(1);
                String description = cursor.getString(2);
                Float cost = cursor.getFloat(3);
                byte[] imageBytes = cursor.getBlob(4);
                Integer foreignKey = cursor.getInt(5);


                //convert bytes back to bitmap
                Bitmap projectImage = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);

                item = new UserProjectItem(itemId,listPosition,description,cost, projectImage,foreignKey);

            }
        }

        cursor.close();
        db.close();
        return item;
    }









    // PROJECT TABLE METHODS ----------------------------

    public UserProject getProjectByListPos1(){
        SQLiteDatabase db = this.getReadableDatabase();

        UserProject project = null;

        // requires item.id followed by foreign key
        Cursor cursor = db.rawQuery(GET_PROJECT_BY_LISTPOS_1,null);

        if (cursor.getCount() > 0){
            while(cursor.moveToNext()){


                Integer id = cursor.getInt(0);
                Integer listPosition = cursor.getInt(1) ;
                String projectName = cursor.getString(2);
                Float totalProjectCost = cursor.getFloat(3);
                byte[] imageBytes = cursor.getBlob(4);       // Grab blob with byte array

                //convert bytes back to bitmap
                Bitmap projectImage = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);

                project = new UserProject(id,listPosition,projectName,totalProjectCost, projectImage);


            }
        }

        cursor.close();
        db.close();
        return project;
    }


    public void projectInsert(String projectName,Bitmap projectImage, ArrayList<Long> foundErrors){
//        ArrayList<Long> foundErrors = new ArrayList<Long>();

        //List pos will = 1 as Default putting it at top of list
        // update all rows listPos to make room for new listPos prior to running this method
        foundErrors.add(incrementAllProjectsListPosition());

        // add to database
        foundErrors.add(projectInsertDataBase(projectName,projectImage));
    }





        //List pos will = 1 as Default putting it at top of list
        // update all rows listPos to make room for new listPos prior to running this method
    public long projectInsertDataBase( String projectName, Bitmap projectImage){

        // add to database
        //create an instance of SQLite database
                                // this. because referring to this class
        SQLiteDatabase db = this.getWritableDatabase();

        byte[] imageInByteArray = byteArrayImageConvert(projectImage);  // converts here to byte array

        // Like and intent setting up for content transfer
                        //(databaseColumn , data)
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PROJECT_NAME, projectName);
        contentValues.put(COL_PROJECT_IMAGE, imageInByteArray);

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
        Cursor cursor = db.rawQuery(GET_PROJECT_LIST_POSITION, null);

        long numOfErrors = 0L;

        if (cursor.getCount() > 0){     // no data found
            UserProject project;
            while(cursor.moveToNext()){      // if their is no next row it returns false


                ContentValues contentValues = new ContentValues();

                Integer currentId = cursor.getInt(0);
                Integer listPosition = cursor.getInt(1);
                listPosition++;

                contentValues.put(COL_PROJECT_LIST_POS,listPosition);

                //set new value to db
                int numOfRowsUpdated = db.update(PROJECT_TABLE_NAME, contentValues, COL_PROJECT_ID + " = ?", new String[]{currentId.toString()});
                if (numOfRowsUpdated != 1){  // should only be 1
                    numOfErrors++;  // if not 1 their was an error.
                }

            }
        }

        cursor.close();
        db.close();
        return numOfErrors;
    };



         // update whole project
    public boolean projectUpdate (Integer id, Integer listPosition, String projectName, Float totalProjectCost, Bitmap projectImage){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PROJECT_ID, id);
        contentValues.put(COL_PROJECT_LIST_POS, listPosition);
        contentValues.put(COL_PROJECT_NAME, projectName);
        contentValues.put(COL_PROJECT_TOTAL_COST, totalProjectCost);
        contentValues.put(COL_PROJECT_IMAGE, byteArrayImageConvert(projectImage));

        int numOfRowsUpdated = db.update(PROJECT_TABLE_NAME, contentValues, COL_PROJECT_ID + " = ?", new String[]{id.toString()});
        db.close();
        return numOfRowsUpdated == 1;
    }


    // only update project cost
    public boolean projectUpdateCost(Integer id, Float newProjectCost){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PROJECT_TOTAL_COST, newProjectCost);

        int numOfRowsUpdated = db.update(PROJECT_TABLE_NAME, contentValues, COL_PROJECT_ID + " = ?", new String[]{id.toString()});
        db.close();
        return  (numOfRowsUpdated == 1);
    }

        // only update projectName
    public boolean projectUpdateName (Integer id, String projectName){
        SQLiteDatabase db = this.getWritableDatabase();

        // Update needs ID to work basic stuff
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PROJECT_NAME, projectName);

        int numOfRowsUpdated = db.update(PROJECT_TABLE_NAME, contentValues, COL_PROJECT_ID + " = ?", new String[]{id.toString()});
        db.close();
        return (numOfRowsUpdated == 1);
    }

         // only update project image
    public boolean projectUpdateImage (Integer id, Bitmap projectImage){
        SQLiteDatabase db = this.getWritableDatabase();

        byte[] imageInByteArray = byteArrayImageConvert(projectImage);  // converts here to byte array

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PROJECT_IMAGE, imageInByteArray);

        int numOfRowsUpdated = db.update(PROJECT_TABLE_NAME, contentValues, COL_PROJECT_ID + " = ?", new String[]{id.toString()});
        db.close();
        return (numOfRowsUpdated == 1);
    }


        // update list pos  --  keep in mind <other projects list pos> <the list reads ASC>
    public Long projectUpdateListPositionToFirst(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PROJECT_LIST_POS, 1); // set new list position to first

        int numOfRowsUpdated = db.update(PROJECT_TABLE_NAME, contentValues, COL_PROJECT_ID + " = ?", new String[]{id.toString()});
        db.close();
        if (numOfRowsUpdated == 1){
            return 1L;
        }else{
            return -1L;
        }

    }

    public boolean projectDelete (Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        int numOfRowsDeleted = db.delete(PROJECT_TABLE_NAME, COL_PROJECT_ID+ " = ?", new String[]{id.toString()});
        return  (numOfRowsDeleted == 1);
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

        db.close();
        cursor.close();
        return projects;
    };




    public UserProject getProject(Integer id){
        SQLiteDatabase db = this.getReadableDatabase();

        UserProject project = null;

        Cursor cursor = db.rawQuery(GET_PROJECT, new String[]{id.toString()});

        if (cursor.getCount() > 0){
            while(cursor.moveToNext()){

                Integer listPosition = cursor.getInt(1) ;
                String projectName = cursor.getString(2);
                Float totalProjectCost = cursor.getFloat(3);
                byte[] imageBytes = cursor.getBlob(4);

                //convert bytes back to bitmap
                Bitmap projectImage = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);

                project = new UserProject(id,listPosition,projectName,totalProjectCost, projectImage);

            }
        }

        cursor.close();
        db.close();
        return project;
    }




    // additional methods ------------------------------------

    // convert bitmap to byte array
    private byte[] byteArrayImageConvert(Bitmap currentBitmap) {
        // convert bitmap image

        // holder for return
        byte[] imageInByteArray;

        // take current bitmap and compress
        objectByteArrayOutputStream = new ByteArrayOutputStream();
        currentBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);

        imageInByteArray = objectByteArrayOutputStream.toByteArray();

        return imageInByteArray;
    }


}
