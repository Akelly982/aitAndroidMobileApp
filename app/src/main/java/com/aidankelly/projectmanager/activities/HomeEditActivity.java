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
import com.aidankelly.projectmanager.recyclerview.OnHomeRVListener;
import com.aidankelly.projectmanager.services.DataService;

import java.util.List;

public class HomeEditActivity extends AppCompatActivity implements OnHomeEditRVListener {

    private DataService myDataService;
    private List<UserProject> projects;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_edit);

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
    public void onProjectNameChangeClick(UserProject project) {

    }

    @Override
    public void onProjectDescriptionChangeClick(UserProject project) {

    }

    @Override
    public void onProjectSetToTopClick(UserProject project) {

    }

    @Override
    public void onProjectDeleteClick(UserProject project) {
                                                    //homeEditActivity.this
        Intent goToDeleteConfirmation = new Intent(this,deleteConfirmation.class);
        goToDeleteConfirmation.putExtra(UserProject.USER_PROJECT_KEY,project);
        startActivityForResult(goToDeleteConfirmation, Constants.DELETE_CONFIRMATION_ACTIVITY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.DELETE_CONFIRMATION_ACTIVITY_CODE){
            if (resultCode == RESULT_OK){
                deleteProject(data);
            }
        }
    }

    private void deleteProject(Intent data) {
                                 // do not forget to cast data type when getting data from intent
        UserProject project = (UserProject) data.getSerializableExtra(UserProject.USER_PROJECT_KEY);
        myDataService.deleteProject(project);
    }
}
