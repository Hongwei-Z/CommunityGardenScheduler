package com.CSCI3130.gardenapp.task_view_list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.CSCI3130.gardenapp.R;

/**
 * Holds the views associated with each card in the the task list
 * @author Elizabeth Eddy
 * @see RecyclerView.ViewHolder
 */
public class TaskViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView date;
    private final TextView priority;
    private final ImageView userProfile;

    /**
     * Constructor for the TaskViewHolder which assigns the OnItemClickListener and View associated to a task
     * @param itemView View associated to the task
     * @param listener OnItemClickListener associated to the task
     * @see TaskAdapter.OnItemClickListener
     */
    protected TaskViewHolder(View itemView, final TaskAdapter.OnItemClickListener listener) {
        super(itemView);

        name = itemView.findViewById(R.id.task_name);
        date = itemView.findViewById(R.id.task_date);
        priority = itemView.findViewById(R.id.task_priority);
        userProfile = itemView.findViewById(R.id.task_user_profile);

        itemView.setOnClickListener(v -> {
            if (listener != null) {
                int position = getAdapterPosition(); // gets task index for
                if (position != RecyclerView.NO_POSITION) { // checking to make sure user actually clicked a task
                    listener.onItemClick(position); //pass position to TaskViewList
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

