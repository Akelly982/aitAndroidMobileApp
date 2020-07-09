package com.aidankelly.projectmanager.services;


import android.content.Context;

import com.aidankelly.projectmanager.database.ProjectDatabaseHelper;
import com.aidankelly.projectmanager.entities.UserProject;
import com.aidankelly.projectmanager.entities.UserProjectItem;

import java.util.ArrayList;
import java.util.List;


public class DataService {

    private  ProjectDatabaseHelper sqlite;


    public void init(Context context){
        sqlite = sqlite.getInstance(context);
    }




    // project   ( home activity etc)  ----------------------
        // sql insert returns long
    public void addProject(UserProject myProject, ArrayList<Long> errorList){
        // returns list holding error info  --  index 0 = list increment method    index 1 = database add method
        sqlite.projectInsert(myProject.getProjectName(), errorList);

    }

    public boolean updateProject(UserProject myProject){
        return sqlite.projectUpdate(myProject.getId(),myProject.getListPosition(),myProject.getProjectName(),myProject.getTotalProjectCost(),myProject.getProjectDirectory(),myProject.getHomeImageFileName());
    }

    public boolean updateProjectImg(UserProject myProject){
        return sqlite.projectUpdateImage(myProject.getId(),myProject.getHomeImageFileName());
    }


    public void setProjectToTop(UserProject project, ArrayList<Long> errorList){
        errorList.add(sqlite.incrementAllProjectsListPosition());
        errorList.add(sqlite.projectUpdateListPositionToFirst(project.getId()));
    }

    public boolean updateProjectName(UserProject myProject){
        return sqlite.projectUpdateName(myProject.getId(),myProject.getProjectName());
    }

    public boolean updateProjectCost(UserProject myProject){
        return sqlite.projectUpdateCost(myProject.getId(),myProject.getTotalProjectCost());
    }

    public boolean deleteProject(Integer id){
        return sqlite.projectDelete(id);
    }

    public List<UserProject> getProjects(){
        List<UserProject> myProjects = sqlite.getProjects();
        return myProjects;
    }

    public UserProject getProject(Integer id){
        return sqlite.getProject(id);
    }

    public UserProject getProjectByListPos1(){
        return sqlite.getProjectByListPos1();
    }





    // project Items   (project activity etc) ------------------------------------

    public UserProjectItem getItemByListPos1(){
        return sqlite.getItemByListPos1();
    }

    public void addItem(UserProjectItem myItem, UserProject parentProject ,ArrayList<Long> errorList){
        // returns list holding error info  --  index 0 = list increment method    index 1 = database add method
        sqlite.itemInsert(myItem.getDescription(), myItem.getCost(), myItem.getImageFileName(), parentProject.getId(), errorList);

    }

    public boolean deleteItem(Integer id){
        return sqlite.itemDelete(id);
    }

    public List<UserProjectItem> getAllItemsOfProject(UserProject parentProject){
        List<UserProjectItem> myProjectItems = sqlite.getItems(parentProject.getId());
        return myProjectItems;
    }

    public UserProjectItem getItem(Integer id){
        return sqlite.getItem(id);
    }

    public int cleanProjectList() {
        return sqlite.allProjectsListPosClean();
    }


    public boolean itemUpdateImgPath(UserProjectItem myItem){
        return sqlite.itemUpdateImgPath(myItem.getId(),myItem.getImageFileName());
    }
//
//
//    public void setItemToTop(UserProject project, ArrayList<Long> errorList){
//
//    }
//
//    public boolean updateItemDesc(UserProject myProject){
//        return false;
//    }


}
