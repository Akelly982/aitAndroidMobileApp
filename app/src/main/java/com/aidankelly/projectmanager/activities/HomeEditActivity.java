package com.aidankelly.projectmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;
import com.aidankelly.projectmanager.recyclerview.HomeEditRecyclerViewAdapter;
import com.aidankelly.projectmanager.services.DataService;

import java.util.List;

public class HomeEditActivity extends AppCompatActivity {

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
        HomeEditRecyclerViewAdapter adapter = new HomeEditRecyclerViewAdapter(projects,this);
        HomeEditRecyclerView.setAdapter(adapter);





        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });










    }


}
