package com.aidankelly.projectmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aidankelly.projectmanager.R;

public class SearchHomeActivity extends AppCompatActivity {

    Button searchByCostDSC;
    Button exitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_home);


        searchByCostDSC = findViewById(R.id.searchHomeMostExpensiveButton);
        exitButton = findViewById(R.id.searchHomeExitButton);




        searchByCostDSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent goToSearchByCostDSC = new Intent(SearchHomeActivity.this, )
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });



    }
}
