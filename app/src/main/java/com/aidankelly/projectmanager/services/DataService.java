package com.aidankelly.projectmanager.services;


import android.content.Context;

import com.aidankelly.projectmanager.database.ProjectDatabaseHelper;
import com.aidankelly.projectmanager.entities.UserProject;

import java.util.ArrayList;


public class DataService {

    private  ProjectDatabaseHelper sqlite;


    public void init(Context context){
        sqlite = sqlite.getInstance(context);
    }


        // sql insert returns long
    public ArrayList addProject(UserProject myProject){
        ArrayList foundErrors = new ArrayList<Long>();

        // returns list holding error info  --  index 0 = list increment method    index 1 = database add method
        foundErrors = sqlite.projectInsert(myProject.getProjectName(),myProject.getProjectImage());

        return  foundErrors;
    }

    public boolean updateProjectImg(UserProject myProject){
        return sqlite.projectUpdateImage(myProject.getId(),myProject.getProjectImage());
    }

    public boolean updateProjectName(UserProject myProject){
        return sqlite.projectUpdateName(myProject.getId(),myProject.getProjectName());
    }

    public boolean deleteProject(UserProject myProject){
        return sqlite.projectDelete(myProject.getId());
    }


}
