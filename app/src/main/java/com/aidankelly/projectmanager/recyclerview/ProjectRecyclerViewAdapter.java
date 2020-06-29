package com.aidankelly.projectmanager.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aidankelly.projectmanager.R;
import com.aidankelly.projectmanager.entities.UserProject;
import com.aidankelly.projectmanager.entities.UserProjectItem;

import java.util.List;

public class ProjectRecyclerViewAdapter extends RecyclerView.Adapter<ProjectViewHolder> {

    // needs to know the parent project for its directory name
    private UserProject parentProject;

    private List<UserProjectItem> projectItems;
    private Context context;
    private OnProjectRVListener onProjectRVListener;

    public ProjectRecyclerViewAdapter(List<UserProjectItem> projectItems, Context context, OnProjectRVListener onProjectRVListener, UserProject parentProject) {
        this.projectItems = projectItems;
        this.context = context;
        this.onProjectRVListener = onProjectRVListener;
        this.parentProject = parentProject;
    }

    @NonNull
    @Override     // gets confusing but     activity name = project   (but the RV is used to show project items)
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View projectItemViewItem = inflater.inflate(R.layout.recycler_item_view_project,parent,false);
        ProjectViewHolder projectItemViewHolder = new ProjectViewHolder(projectItemViewItem, context);

        return projectItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        UserProjectItem item = projectItems.get(position);
        holder.updateProjectItem(item, parentProject);
        holder.bindUpdateButtons(item, onProjectRVListener, position);
    }

    @Override
    public int getItemCount() {
        return projectItems.size();
    }



    // updating the RV
    public void deleteItemByIndex(int position){
        projectItems.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(UserProjectItem item) {
        int position = 0;
        projectItems.add(position,item);
        notifyItemInserted(position);
    }
    public List<UserProjectItem> getRvList() {
        return projectItems;
    }

}
