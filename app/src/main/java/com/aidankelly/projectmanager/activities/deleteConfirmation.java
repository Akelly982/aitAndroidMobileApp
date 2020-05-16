package com.aidankelly.projectmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;

public class deleteConfirmation extends AppCompatActivity {

    Button deleteButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_confirmation);

        deleteButton = findViewById(R.id.deleteConfirmationDeleteButton);
        cancelButton = findViewById(R.id.deleteConfirmationCancelButton);



        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent goingBackDelete = new Intent();
//                goingBackDelete.putExtra(UserProject.USER_PROJECT_KEY, project);
//                setResult(RESULT_OK,goingBackDelete);
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }
}
