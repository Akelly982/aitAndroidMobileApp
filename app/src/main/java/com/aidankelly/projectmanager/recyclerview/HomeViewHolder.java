package com.aidankelly.projectmanager.recyclerview;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;

public class HomeViewHolder extends RecyclerView.ViewHolder {

    private ImageView projectImageView;
    private TextView projectNameTextView;
    private TextView projectCostTextView;
    private Button enterProjectButton;


    // maps UI components to the XML Layout.
    public HomeViewHolder(@NonNull View itemView) {
        super(itemView);

        // link ui components
        projectImageView = itemView.findViewById(R.id.homeProjectImageImageView);
        projectNameTextView = itemView.findViewById(R.id.homeProjectNameTextView);
        projectCostTextView = itemView.findViewById(R.id.homeProjectCostTextView);
        enterProjectButton = itemView.findViewById(R.id.homeEnterProjectButton);

    }

    // puts data into my ViewHolder
    public void updateProject(UserProject project){
        projectImageView.setImageBitmap(project.getProjectImage());
        projectNameTextView.setText(project.getProjectName());
        projectCostTextView.setText("$ " + project.getTotalProjectCost().toString());


    }



    /**
     * Bind evey project with a listener, to be used when the user clicks a particular project in the recyclerView
     */

    public void bindUpdateProjectButtons(final UserProject project, final OnHomeRVListener onHomeRVListener){
        enterProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHomeRVListener.onProjectEnterClick(project);
            }
        });
    }


//    /**
//     * Bind evey project with a listener, to be used when the user clicks a particular project in the recyclerView
//     */
//    public void bind (final UserProject project, final OnRecyclerViewListener onRecyclerViewListener){
//        this.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Snackbar.make(v, " implement enter project id:  " + project.getId().toString() , Snackbar.LENGTH_SHORT).show();
//            }
//        });
//    }

}
