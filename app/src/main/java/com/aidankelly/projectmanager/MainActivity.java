package com.aidankelly.projectmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aidankelly.projectmanager.activities.HomeActivity;
import com.aidankelly.projectmanager.entities.Constants;

import static com.aidankelly.projectmanager.entities.Constants.HOME_ACTIVITY_CODE;

public class MainActivity extends AppCompatActivity {


    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(start);
            }
        });

    }
}
