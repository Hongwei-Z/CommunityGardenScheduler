package com.CSCI3130.gardenapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private ArrayList<Task> taskList;

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView date;
        private TextView priority;
        private ImageView userProfile;
        private Button registerButton;

        private TaskViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.task_name);
            date = itemView.findViewById(R.id.task_date);
            priority = itemView.findViewById(R.id.task_priority);
            userProfile = itemView.findViewById(R.id.task_user_profile);
            registerButton = itemView.findViewById(R.id.registerButton);
        }

        public void setName(String name){
            this.name.setText(name);
        }

        public void setDate(String date){
            this.date.setText(date);
        }

        public void setPriority(int priority){
            this.priority.setText(Integer.toString(priority));
            int color = R.color.colorPriority + priority;
            this.priority.setBackgroundResource(color);
        }

        public void setUser(String user){
            if (!user.equals("")) {
                this.userProfile.setImageResource(R.drawable.profile_icon);
                this.registerButton.setVisibility(itemView.GONE);
            }
            else {
                this.registerButton.setVisibility(itemView.VISIBLE);
            }
        }
    }

    TaskAdapter(ArrayList<Task> tasks){
        taskList = tasks;
    }

    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position){
        Task task = taskList.get(position);
        holder.setName(task.getName());
        holder.setDate(task.getDate());
        holder.setPriority(task.getPriority());
        holder.setUser(task.getUser());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}

