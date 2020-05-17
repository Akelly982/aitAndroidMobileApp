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
import com.aidankelly.projectmanager.entities.Constants;
import com.aidankelly.projectmanager.entities.UserProject;
import com.aidankelly.projectmanager.recyclerview.HomeEditRecyclerViewAdapter;
import com.aidankelly.projectmanager.recyclerview.OnHomeEditRVListener;
import com.aidankelly.projectmanager.services.DataService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.aidankelly.projectmanager.entities.Constants.CHANGE_TEXT_ACTIVITY_CODE;
import static com.aidankelly.projectmanager.entities.Constants.DELETE_CONFIRMATION_ACTIVITY_CODE;

public class HomeEditActivity extends AppCompatActivity implements OnHomeEditRVListener {

    private DataService myDataService;
    private List<UserProject> projects;
    private Button exitButton;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_edit);

        rootView = findViewById(android.R.id.content).getRootView();
        exitButton = findViewById(R.id.homeEditExitButton);


        //Load Data from the database
        myDataService = new DataService();
        myDataService.init(this);


        //implementing the recycler view
        RecyclerView HomeEditRecyclerView = findViewById(R.id.homeEditRecyclerView);

        //set up linear layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        HomeEditRecyclerView.setLayoutManager(linearLayoutManager);

        // set up adapter
        projects = myDataService.getProjects();
        HomeEditRecyclerViewAdapter adapter = new HomeEditRecyclerViewAdapter(projects,this, this);
        HomeEditRecyclerView.setAdapter(adapter);





        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
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
            }
        }
        if (requestCode == CHANGE_TEXT_ACTIVITY_CODE){
            if (resultCode == RESULT_OK){
                changeProjectName(data);
            }
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

    }

    private void deleteProject(Intent data) {
        // do not forget to cast data type when getting data from intent
        Integer projectId = data.getIntExtra(UserProject.USER_PROJECT_ID_KEY, -1);

        //update db
        Boolean result = myDataService.deleteProject(projectId);

        // display result
        if (result){
            Snackbar.make(rootView, " project deleted: "  , Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(rootView, " project not deleted: " , Snackbar.LENGTH_SHORT).show();
        }


    }



    // RV buttons
    @Override
    public void onProjectNameChangeClick(UserProject project) {
        Intent goToChangeName = new Intent(this, changeTextActivity.class);
        goToChangeName.putExtra(UserProject.USER_PROJECT_PROJECT_NAME ,project.getProjectName());
        goToChangeName.putExtra(UserProject.USER_PROJECT_ID_KEY, project.getId());
        startActivityForResult(goToChangeName,CHANGE_TEXT_ACTIVITY_CODE);
    }

    @Override
    public void onProjectImageChangeClick(UserProject project) {
//          Intent goToChangeImage = new Intent(this, changeImageActivity.class);
//          goToChangeImage.putExtra(UserProject.USER_PROJECT_ID_KEY,project.getId());
//          goToChangeImage.putExtra()
    }


    @Override
    public void onProjectSetToTopClick(UserProject project) {
        ArrayList<Long> errorList = new ArrayList<Long>();
        myDataService.setProjectToTop(project,errorList);
        if (errorList.get(0) > 0L){  // increment errors
            Snackbar.make(rootView, " project set top increment errors = " + errorList.get(0).toString() , Snackbar.LENGTH_SHORT).show();
        }
        if (errorList.get(1) == -1){ // set top errors
            Snackbar.make(rootView, " project set self top list pos error " + errorList.get(1).toString() , Snackbar.LENGTH_SHORT).show();
        }
        Snackbar.make(rootView, " Set top complete ", Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onProjectDeleteClick(UserProject project) {

                    // note atm cant pass the class due to bitmap variable is not serializable
        Intent goToDeleteConfirmation = new Intent(HomeEditActivity.this, deleteConfirmationActivity.class);
        goToDeleteConfirmation.putExtra(UserProject.USER_PROJECT_ID_KEY,project.getId());
        startActivityForResult(goToDeleteConfirmation, DELETE_CONFIRMATION_ACTIVITY_CODE);

    }


}
