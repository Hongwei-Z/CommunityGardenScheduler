package com.CSCI3130.gardenapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.CSCI3130.gardenapp.util.data.Task;

import com.CSCI3130.gardenapp.util.DateFormatUtils;
import java.util.ArrayList;

/**
 * TaskAdapter.java - a class used to manage the list of tasks displayed to the user and the
 * interactions associated with the list
 * the list of tasks
 * @author  Elizabeth Eddy & Logan Sutherland
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private ArrayList<Task> taskList;
    private OnItemClickListener mListener;

    public interface  OnItemClickListener { //interface so we can interact with TaskAdapter from TaskViewList
        void onItemClick(int position);
    }

    /**
     * Sets the click listener for a task in the list
     * @param listener listen for click
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * Holds the views associated with each card in the the task list
     */
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView date;
        private TextView priority;
        private ImageView userProfile;
        private RelativeLayout clickList;

        private TaskViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.task_name);
            date = itemView.findViewById(R.id.task_date);
            priority = itemView.findViewById(R.id.task_priority);
            userProfile = itemView.findViewById(R.id.task_user_profile);
            clickList = itemView.findViewById(R.id.clicklist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition(); // gets task index for
                        if (position != RecyclerView.NO_POSITION) { // checking to make sure user actually clicked a task
                            listener.onItemClick(position); //pass position to TaskViewList
                        }
                    }
                }
            });
        }

        /**
         * Set task name text in the textView
         * @param name name of the task
         */
        public void setName(String name){
            this.name.setText(name);
        }

        /**
         * Set task date text in the textView
         * @param date date of the task
         */
        public void setDate(String date){
            this.date.setText(date);
        }

        /**
         * Set task priority text in the textView and update the colour to match
         * @param priority priority of the task
         */
        public void setPriority(int priority){
            this.priority.setText(Integer.toString(priority));
            int color = R.color.colorPriority + priority;
            this.priority.setBackgroundResource(color);
        }

        /**
         * Set task profile to the assigned user if exists, otherwise leave blank
         * @param user user assigned to task
         */
        public void setUser(String user){
            if (user != null && !user.equals("")) {
                this.userProfile.setImageResource(R.drawable.profile_icon);
            }
        }
    }

    public TaskAdapter(ArrayList<Task> tasks){
        taskList = tasks;
    }

    /**
     * Overrides onCreateViewHolder to add tasks the recyclerView
     * @param parent the viewGroup
     * @param viewType the type of view
     * @return TaskViewHolder containing the tasks
     */
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
        return new TaskViewHolder(view, mListener);
    }

    /**
     * Sets the task details in the tasks CardView
     * @param holder holds views associated with task
     * @param position index of the task in list
     */
    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position){
        Task task = taskList.get(position);
        holder.setName(task.getName());
        long date = TaskViewList.activeTaskListContext.equals("taskHistory")
                ? task.getDateCompleted()
                : task.getDateDue();
        if (date != -1) {
            holder.setDate( (TaskViewList.activeTaskListContext.equals("taskHistory")
                    ? "Completed: "
                    : "Due: ")
                    + DateFormatUtils.getDateFormatted(date));
        } else {
            holder.setDate("");
        }
        holder.setPriority(task.getPriority());
        holder.setUser(task.getUser());

        holder.clickList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), TaskDetailInfo.class);
                i.putExtra("Iname",taskList.get(position));
                view.getContext().startActivity(i);
            }
        });

    }

    /**
     * Gets the number of tasks in the task list
     * @return size of task list
     */
    @Override
    public int getItemCount() {
        return taskList.size();
    }
}

