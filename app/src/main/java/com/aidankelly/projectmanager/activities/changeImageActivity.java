package com.aidankelly.projectmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aidankelly.projectmanager.R;

public class changeImageActivity extends AppCompatActivity {

    private Button exitButton;
    private Button cancelButton;
    private Button importImageButton;
    private Button setImageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);

        exitButton = findViewById(R.id.changeImageExitButton);
        cancelButton = findViewById(R.id.changeImageCancelButton);
        importImageButton = findViewById(R.id.changeImageImportImageButton);
        setImageButton = findViewById(R.id.changeImageSetImageButton);












        importImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        setImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

    }

    private void exit() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
