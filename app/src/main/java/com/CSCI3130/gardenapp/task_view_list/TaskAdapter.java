package com.CSCI3130.gardenapp.task_view_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.util.DateFormatUtils;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * TaskAdapter.java - a class used to manage the list of tasks displayed to the user and the
 * interactions associated with the list
 * the list of tasks
 *
 * @author Elizabeth Eddy & Logan Sutherland
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private final ArrayList<Task> taskList;
    private final String activeTaskListContext;
    private OnItemClickListener mListener;

    public TaskAdapter(ArrayList<Task> tasks, String activeTaskListContext) {
        this.activeTaskListContext = activeTaskListContext;
        taskList = tasks;
    }

    /**
     * Sets the click listener for a task in the list
     *
     * @param listener listen for click
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * Overrides onCreateViewHolder to add tasks the recyclerView
     *
     * @param parent   the viewGroup
     * @param viewType the type of view
     * @return TaskViewHolder containing the tasks
     */
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
        return new TaskViewHolder(view, mListener);
    }

    /**
     * Sets the task details in the tasks CardView
     *
     * @param holder   holds views associated with task
     * @param position index of the task in list
     */
    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.setName(task.getName());
        //current date
        long nowdate = System.currentTimeMillis();
        long date = activeTaskListContext.equals("taskHistory")
                ? task.getDateCompleted()
                : task.getDateDue();
        if (date != -1) {
            holder.setDate((activeTaskListContext.equals("taskHistory")
                    ? "Completed: "
                    : "Due: ")
                    + DateFormatUtils.getDateFormatted(date));
        } else {
            if (task.getWeatherTrigger() != null) {
                WeatherCondition trig = task.getWeatherTrigger();
                switch (trig) {
                    case DRY:
                        holder.setDate("Dry Weather");
                        break;
                    case WINDY:
                        holder.setDate("Windy Weather");
                        break;
                    case COLD:
                        holder.setDate("Cold Weather");
                        break;
                    case HOT:
                        holder.setDate("Hot Weather");
                        break;
                    case RAIN:
                        holder.setDate("Rainy Weather");
                        break;
                    default:
                        holder.setDate("");
                        break;
                }
            }
        }

        /*
         * using due date - current date
         * when deadline is more than 0 seconds, equal and less than 1 day, it will show red exclamation point
         * when deadline is larger than 1 day but equal and less than 3 days, it will show yellow exclamation point
         * when deadline is more than 3 days, hidden symbol
         */

        long diff_date = TimeUnit.MILLISECONDS.toDays(date - nowdate);
        if (diff_date == 1) {
            holder.setDuesymbol(R.drawable.red);
        } else if (diff_date == 2 || diff_date ==3 ){
            holder.setDuesymbol(R.drawable.yellow);
        } else {
            holder.setHideDuesymbol();
        }
        holder.setPriority(task.getPriority());
        holder.setUser(task.getUser());
    }

    /**
     * Gets the number of tasks in the task list
     *
     * @return size of task list
     */
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public interface OnItemClickListener { //interface so we can interact with TaskAdapter from TaskViewList
        void onItemClick(int position);
    }
}

