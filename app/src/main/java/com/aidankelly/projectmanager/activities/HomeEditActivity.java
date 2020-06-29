package com.aidankelly.projectmanager.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.ImageManager;
import com.aidankelly.projectmanager.entities.UserProject;
import com.aidankelly.projectmanager.recyclerview.HomeEditRecyclerViewAdapter;
import com.aidankelly.projectmanager.recyclerview.OnHomeEditRVListener;
import com.aidankelly.projectmanager.services.DataService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.aidankelly.projectmanager.entities.Constants.CHANGE_IMAGE_ACTIVITY_CODE;
import static com.aidankelly.projectmanager.entities.Constants.CHANGE_TEXT_ACTIVITY_CODE;
import static com.aidankelly.projectmanager.entities.Constants.DELETE_CONFIRMATION_ACTIVITY_CODE;

public class HomeEditActivity extends AppCompatActivity implements OnHomeEditRVListener {

    private DataService myDataService;
    private List<UserProject> projects;
    private Button exitButton;
    private View rootView;
    private HomeEditRecyclerViewAdapter adapter;
    private UserProject editProjectOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_edit);

        rootView = findViewById(android.R.id.content).getRootView();
        exitButton = findViewById(R.id.homeEditExitButton);


        //initiate Data from the database
        myDataService = new DataService();
        myDataService.init(this);


        //implementing the recycler view
        RecyclerView HomeEditRecyclerView = findViewById(R.id.homeEditRecyclerView);

        //set up linear layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        HomeEditRecyclerView.setLayoutManager(linearLayoutManager);

        // load from database and set up adapter
        projects = myDataService.getProjects();
        adapter = new HomeEditRecyclerViewAdapter(projects,this, this);
        HomeEditRecyclerView.setAdapter(adapter);





        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });





    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DELETE_CONFIRMATION_ACTIVITY_CODE){
            if (resultCode == RESULT_OK){
                deleteProject(data);
            }else{
                editProjectOriginal = null;
            }
        }
        if (requestCode == CHANGE_TEXT_ACTIVITY_CODE){
            if (resultCode == RESULT_OK){
                changeProjectName(data);
            }else{
                editProjectOriginal = null;
            }
        }
        if (requestCode == CHANGE_IMAGE_ACTIVITY_CODE){
            if (resultCode == RESULT_OK){
                changeProjectImage(data);
            }else{
                editProjectOriginal = null;
            }
        }
    }

    private void changeProjectImage(Intent data) {
        UserProject project = (UserProject) data.getSerializableExtra(UserProject.USER_PROJECT_KEY);

        //RV update                 // get position of the original project
        int RVListPos = adapter.getRvList().indexOf(editProjectOriginal);    // -1 if not in list else // the position of the object
        if (RVListPos != -1){
            project = myDataService.getProject(project.getId());
            adapter.replaceItem(RVListPos,project);  // this should still reload the project with the new image
        }


    }

    private void changeProjectName(Intent data) {
        UserProject project = new UserProject();
        project.setId(data.getIntExtra(UserProject.USER_PROJECT_ID_KEY,-1));
        project.setProjectName(data.getStringExtra(UserProject.USER_PROJECT_PROJECT_NAME));

        //update db
        boolean result = myDataService.updateProjectName(project);

        // display result
        if (result){
            Snackbar.make(rootView, " project name updated ", Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(rootView, " project name not updated ", Snackbar.LENGTH_SHORT).show();
        }

        //RV update                 // get position of the original project
        int RVListPos = adapter.getRvList().indexOf(editProjectOriginal);    // -1 if not in list else // the position of the object
        if (RVListPos != -1){
            project = myDataService.getProject(project.getId());
            adapter.replaceItem(RVListPos,project);
        }

    }

    private void deleteProject(Intent data) {
        // do not forget to cast data type when getting data from intent
        UserProject project = (UserProject) data.getSerializableExtra(UserProject.USER_PROJECT_KEY);

        // remove from RV
        //RV update                 // get position of the original project
        int RVListPos = adapter.getRvList().indexOf(editProjectOriginal);    // -1 if not in list else // the position of the object
        if (RVListPos != -1){
            adapter.deleteItemByIndex(RVListPos);
        }

        // Remove from local
        ImageManager imgManagerDelete = new ImageManager(this);
        boolean result1 = imgManagerDelete.deleteProjectDirectory(project);
        if (result1){
            Snackbar.make(rootView, " project deleted from local: "  , Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(rootView, " project not deleted from local: " , Snackbar.LENGTH_SHORT).show();
        }


        // remove from db
        boolean result = myDataService.deleteProject(project.getId());
        // display result
        if (result){
            Snackbar.make(rootView, " project deleted from db "  , Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(rootView, " project not deleted from db: " , Snackbar.LENGTH_SHORT).show();
        }



    }



    // RV buttons
    @Override
    public void onProjectNameChangeClick(UserProject project) {
        Intent goToChangeName = new Intent(this,ChangeTextActivity.class);
        goToChangeName.putExtra(UserProject.USER_PROJECT_PROJECT_NAME ,project.getProjectName());
        goToChangeName.putExtra(UserProject.USER_PROJECT_ID_KEY, project.getId());
        startActivityForResult(goToChangeName,CHANGE_TEXT_ACTIVITY_CODE);
        editProjectOriginal = project;
    }

    @Override
    public void onProjectImageChangeClick(UserProject project) {
          Intent goToChangeImage = new Intent(this, ChangeImageActivity.class);
          goToChangeImage.putExtra(UserProject.USER_PROJECT_KEY,project);   // I adjusted the UserProject class to not use bitmap but byte[]
          editProjectOriginal = project;
          startActivityForResult(goToChangeImage, CHANGE_IMAGE_ACTIVITY_CODE);

    }


    @Override
    public void onProjectSetToTopClick(UserProject project) {

        //set to top db
        ArrayList<Long> errorList = new ArrayList<Long>();
        myDataService.setProjectToTop(project,errorList);
        if (errorList.get(0) > 0L){  // increment errors
            Snackbar.make(rootView, " project set top increment errors = " + errorList.get(0).toString() , Snackbar.LENGTH_SHORT).show();
        }
        if (errorList.get(1) == -1){ // set top errors
            Snackbar.make(rootView, " project set self top list pos error " + errorList.get(1).toString() , Snackbar.LENGTH_SHORT).show();
        }
        Snackbar.make(rootView, " Set top complete ", Snackbar.LENGTH_SHORT).show();

        //RV update                 // get position of the original project
        int RVListPos = adapter.getRvList().indexOf(project);    // -1 if not in list else // the position of the object
        if (RVListPos != -1){
            adapter.moveItemToFirst(RVListPos,project);
        }



    }

    @Override
    public void onProjectDeleteClick(UserProject project) {

        Intent goToDeleteConfirmation = new Intent(HomeEditActivity.this, DeleteConfirmationActivity.class);
        goToDeleteConfirmation.putExtra(UserProject.USER_PROJECT_KEY,project);
        startActivityForResult(goToDeleteConfirmation, DELETE_CONFIRMATION_ACTIVITY_CODE);
        editProjectOriginal = project;


    }


}
