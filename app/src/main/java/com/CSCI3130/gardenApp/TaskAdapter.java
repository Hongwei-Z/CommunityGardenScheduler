package com.CSCI3130.gardenApp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    ArrayList<Task> taskList;

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        TextView priority;
        ImageView userProfile;

        public TaskViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.task_name);
            date = itemView.findViewById(R.id.task_date);
            priority = itemView.findViewById(R.id.task_priority);
            userProfile = itemView.findViewById(R.id.task_user_profile);
        }

    }

    public TaskAdapter(ArrayList<Task> tasks){
        taskList = tasks;
    }

    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
        TaskViewHolder viewHolder = new TaskViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position){
        Task task = taskList.get(position);
        holder.name.setText(task.getName());
        holder.date.setText(task.getDate());
        holder.priority.setText(task.getPriority());
        if (task.getUser() != null) {
            holder.userProfile.setImageResource(R.drawable.profile_icon);
        }
        int color = R.color.colorPriority + Integer.parseInt(task.getPriority());
        holder.priority.setBackgroundResource(color);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}

