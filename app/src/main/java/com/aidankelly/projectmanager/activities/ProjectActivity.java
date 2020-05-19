package com.aidankelly.projectmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;

public class ProjectActivity extends AppCompatActivity {

    private UserProject project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);




    }
}
