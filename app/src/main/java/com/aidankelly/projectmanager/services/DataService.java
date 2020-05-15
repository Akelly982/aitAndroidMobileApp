package com.aidankelly.projectmanager.services;


import android.content.Context;

import com.aidankelly.projectmanager.database.ProjectDatabaseHelper;
import com.aidankelly.projectmanager.entities.UserProject;

import java.util.ArrayList;
import java.util.List;


public class DataService {

    private  ProjectDatabaseHelper sqlite;

    public void connect(){

    }

    public void disconnect(){

    }


    public void init(Context context){
        sqlite = sqlite.getInstance(context);
    }


        // sql insert returns long
    public void addProject(UserProject myProject, ArrayList<Long> errorList){
        // returns list holding error info  --  index 0 = list increment method    index 1 = database add method
        sqlite.projectInsert(myProject.getProjectName(),myProject.getProjectImage(), errorList);

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

    public List<UserProject> getProjects(){
        List<UserProject> myProjects = sqlite.getProjects();
        return myProjects;
    }

    public UserProject getProject(Integer id){
        return sqlite.getProject(id);
    }


}
