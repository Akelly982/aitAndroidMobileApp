package com.aidankelly.projectmanager.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;

import java.util.List;

public class HomeEditRecyclerViewAdapter extends RecyclerView.Adapter<HomeEditViewHolder> {

    private List<UserProject> projects;
    private Context context;


    public HomeEditRecyclerViewAdapter(List<UserProject> projects, Context context) {
        this.projects = projects;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create the ui from the XML
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // for this you may need to import the class R
        View homeEditViewItem = inflater.inflate(R.layout.recycler_item_view_home_edit, parent, false);

        HomeEditViewHolder homeEditViewHolder = new HomeEditViewHolder(homeEditViewItem);
        return homeEditViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeEditViewHolder holder, int position) {
        UserProject project = projects.get(position);
        holder.updateEditProject(project);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}
