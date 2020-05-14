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

    private OnRecyclerViewListener onRecyclerViewListener;

    private ImageView projectImageView;
    private TextView projectNameTextView;
    private TextView projectCostTextView;

    public Button getEnterProjectButton() {
        return enterProjectButton;
    }

    public void setEnterProjectButton(Button enterProjectButton) {
        this.enterProjectButton = enterProjectButton;
    }

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

    public void BindUpdateProjectButtons(final UserProject project, final OnRecyclerViewListener onRecyclerViewListener){
        enterProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecyclerViewListener.onProjectItemEnterClick(project);
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
