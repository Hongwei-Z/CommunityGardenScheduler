package com.CSCI3130.gardenapp.task_view.task_view_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.task_view.ActiveTaskListContext;
import com.CSCI3130.gardenapp.util.DateFormatUtils;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;
import java.util.ArrayList;

/**
 * TaskAdapter.java - a class used to manage the list of tasks displayed to the user and the
 * interactions associated with the list
 * the list of tasks
 *
 * @author Elizabeth Eddy & Logan Sutherland
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private final ArrayList<Task> taskList;
    private final ActiveTaskListContext activeTaskListContext;
    private OnItemClickListener mListener;

    public TaskAdapter(ArrayList<Task> tasks, ActiveTaskListContext activeTaskListContext) {
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
        long date = activeTaskListContext.equals(ActiveTaskListContext.TASK_HISTORY)
                ? task.getDateCompleted()
                : task.getDateDue();
        if (date != -1) {
            holder.setDate((activeTaskListContext.equals(ActiveTaskListContext.TASK_HISTORY)
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

