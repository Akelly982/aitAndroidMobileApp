package com.aidankelly.projectmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;
import com.google.android.material.snackbar.Snackbar;

public class DeleteConfirmationActivity extends AppCompatActivity {

    private Button deleteButton;
    private Button cancelButton;

    private View rootView;

    Integer projectId = null;

    Integer dataType;
    private static final Integer PROJECT_FOR_DELETE = 1;
    private static final Integer ITEM_OF_PROJECT_FOR_DELETE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_confirmation);

        deleteButton = findViewById(R.id.deleteConfirmationDeleteButton);
        cancelButton = findViewById(R.id.deleteConfirmationCancelButton);
        rootView = findViewById(android.R.id.content).getRootView();



        Intent intentThatCalled = getIntent();

        if (getIntent().hasExtra(UserProject.USER_PROJECT_ID_KEY)){
            projectId = intentThatCalled.getIntExtra(UserProject.USER_PROJECT_ID_KEY, -1);
            dataType = PROJECT_FOR_DELETE;

            // if id gotten is the default -1
            if(projectId == -1){
                Snackbar.make(rootView, " project id not correctly passed == -1 " , Snackbar.LENGTH_SHORT).show();
            }



        }




        deleteButton.setOnClickListener(new View.OnClickListener() {   // if delete return intent
            @Override
            public void onClick(View v) {

                if (dataType == PROJECT_FOR_DELETE){
                    Intent goingBackDelete = new Intent();
                    goingBackDelete.putExtra(UserProject.USER_PROJECT_ID_KEY, projectId);
                    setResult(RESULT_OK,goingBackDelete);
                    finish();
                }

            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {  // cancel
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }
}
