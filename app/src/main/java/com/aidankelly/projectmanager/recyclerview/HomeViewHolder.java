package com.aidankelly.projectmanager.recyclerview;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;
import com.google.android.material.snackbar.Snackbar;

public class HomeViewHolder extends RecyclerView.ViewHolder {


    private ImageView projectImageView;
    private TextView projectNameTextView;
    private TextView projectCostTextView;
    private Button enterProjectButton;

    // maps UI components to the XML Layout.
    public HomeViewHolder(@NonNull View itemView) {
        super(itemView);

        // link ui components
        projectImageView = itemView.findViewById(R.id.projectImageHomeEditImageView);
        projectNameTextView = itemView.findViewById(R.id.projectNameHomeEditTextView);
        projectCostTextView = itemView.findViewById(R.id.projectCostTextView);
        enterProjectButton = itemView.findViewById(R.id.deleteProjectButton);

        enterProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, " Todo implement enter project.... " , Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    // puts data into my ViewHolder
    public void updateProject(UserProject project){
        projectImageView.setImageBitmap(project.getProjectImage());
        projectNameTextView.setText(project.getProjectName());
        projectCostTextView.setText("$ " + project.getTotalProjectCost().toString());

    }


}
