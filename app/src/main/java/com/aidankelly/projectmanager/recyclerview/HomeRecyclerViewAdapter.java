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

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private List<UserProject> projects;
    private Context context;

        // constructor            // it is usual convention to call them the same name
    public HomeRecyclerViewAdapter(List<UserProject> projects, Context context) {
        this.projects = projects;
        this.context = context;
    }

    @NonNull
    @Override  // creates the viewHolder
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create the ui from the XML
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // for this you may need to import the class R
        View projectViewItem = inflater.inflate(R.layout.recycler_item_view_home, parent, false);

        HomeViewHolder homeViewHolder = new HomeViewHolder(projectViewItem);

        return homeViewHolder;
    }

    @Override   // sets the data in the view holder
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        UserProject project = projects.get(position);
        holder.updateProject(project);  // uses updateProject from ViewHolder class
    }

    @Override   // tell the adapter how big the data list is..
    public int getItemCount() {
        return projects.size();
    }
}
