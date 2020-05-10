package com.aidankelly.projectmanager.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;
import com.aidankelly.projectmanager.services.DataService;

import static com.aidankelly.projectmanager.entities.Constants.NEW_PROJECT_ACTIVITY_CODE;

public class HomeActivity extends AppCompatActivity {

    private DataService projectDataService;

    Button newProjectButton;
    Button optionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        newProjectButton = findViewById(R.id.newProjectButton);
        optionsButton = findViewById(R.id.returnButton);


        newProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNewProjectActivity = new Intent(HomeActivity.this, newProjectActivity.class);
                startActivity(goToNewProjectActivity);
            }
        });

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make the options menu
            }
        });


        //Load Data from the database
        projectDataService = new DataService();
        projectDataService.init(this);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_PROJECT_ACTIVITY_CODE){
            if (resultCode == RESULT_OK){
                addNewProjectToDatabase(data);
            }
        }
    }

    private void addNewProjectToDatabase(Intent data) {
        // add returned data to persistant data in database


    }
}
