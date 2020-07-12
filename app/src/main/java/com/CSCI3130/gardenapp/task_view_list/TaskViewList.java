package com.CSCI3130.gardenapp.task_view_list;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CSCI3130.gardenapp.FilterPopUp;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.SortCategory;
import com.CSCI3130.gardenapp.SortOrder;
import com.CSCI3130.gardenapp.create_task.CreateTaskActivity;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.db.TaskDatabase;
import com.google.android.material.snackbar.Snackbar;


/**
 * Activity class to process the task data and populate the task list
 *
 * @author Elizabeth Eddy and Logan Sutherland
 */
public class TaskViewList extends AppCompatActivity {

    private static Context mContext;
    protected RecyclerView recyclerView;
    TaskDatabase db;
    private ActiveTaskListContext lastContext = ActiveTaskListContext.ALL_TASKS; //default to all tasks
    private RecyclerView.LayoutManager layoutManager;
    Dialog myDialog;
    public static ActiveTaskListContext activeTaskListContext;

    //sorting and filtering values
    SortCategory sortCat;
    SortOrder sortOrder;
    int priorityFilter;
    long startDate;
    long endDate;

    /**
     * Get the activity context
     *
     * @return context
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * Set the activity context
     *
     * @param mContext context
     */
    public void setContext(Context mContext) {
        TaskViewList.mContext = mContext;
    }


    /**
     * Actions for when activity is created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view_list);
        myDialog = new Dialog(this);
        setContext(this);
        if (getIntent().getBooleanExtra("result", false)) {
            Snackbar.make(findViewById(R.id.task_view_list), "Success!", Snackbar.LENGTH_SHORT).show();
        }

        ActiveTaskListContext activeTaskListContext = (ActiveTaskListContext) getIntent().getSerializableExtra("activeTaskListContext");
        if (activeTaskListContext == null) {
            activeTaskListContext = lastContext;
        }
        TextView toolbarTitle = findViewById(R.id.page_name);
        lastContext = activeTaskListContext;
        SortOrder sortOrderDefault = SortOrder.ASCENDING;

        switch (activeTaskListContext) {
            case MY_TASKS:
                toolbarTitle.setText("My Tasks");
                break;
            case OPEN_TASKS:
                toolbarTitle.setText("Open Tasks");
                break;
            case TASK_HISTORY:
                sortOrderDefault = SortOrder.DESCENDING;
                toolbarTitle.setText("Task History");
                break;
            default:
                toolbarTitle.setText("All Tasks");
                break;
        }

        db = new TaskDatabase();
        db.setDbRead(activeTaskListContext);

        recyclerView = findViewById(R.id.recycleview_tasks);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get filter values
        priorityFilter = getIntent().getIntExtra("priority", -1);
        startDate = getIntent().getLongExtra("startDate", 0l);
        endDate = getIntent().getLongExtra("endDate", Long.MAX_VALUE);

        //account for sorting
        if (getIntent().getSerializableExtra("sort_category") != null) {
            sortCat = (SortCategory) getIntent().getSerializableExtra("sort_category");
        } else {
            sortCat = SortCategory.NONE;
        }


        if (getIntent().getSerializableExtra("sort_order") != null) {
            SortOrder sortValue = (SortOrder) getIntent().getSerializableExtra("sort_order");
            if (sortValue != SortOrder.NONE)
                sortOrder = sortValue;
            else {
                sortOrder = sortOrderDefault;
            }
        } else {
            sortOrder = sortOrderDefault;
        }

        db.getDbRead().addValueEventListener(
                db.getTaskData(recyclerView, activeTaskListContext, sortCat, sortOrder, priorityFilter, startDate, endDate));

    }

    /**
     * Method opens popup with filters and sorting options
     * @param view
     */
    public void PopUp(View view) {
        Intent intent = new Intent(this, FilterPopUp.class);
        startActivity(intent);
    }

    /**
     * On Press method for the hovering + button to link to the CreateTask screen.
     *
     * @param v View belonging to the + button
     */
    public void onButtonPress(View v) {
        Intent i = new Intent(this, CreateTaskActivity.class);
        i.putExtra(getString(R.string.editSetting_extra), false);
        startActivity(i);
    }

    /**
     * Gets sort category of list
     * @return SortCategory enum
     */
    public SortCategory getSortCategory() { return sortCat; }

    /**
     * Sets sort category of list
     * @param sortCategory - SortCategory enum value
     */
    public void setSortCategory(SortCategory sortCategory) { this.sortCat = sortCategory; }

    /**
     * Gets sort order of list
     * @return SortOrder enum
     */
    public SortOrder getSortOrder() { return sortOrder; }

    /**
     * Sets sort order of list
     * @param sortOrder - SortOrder enum value
     */
    public void setSortOrder(SortOrder sortOrder) { this.sortOrder = sortOrder; }


    /**
     * Gets priority value for filtering
     * @return int - priority value
     */
    public int getPriorityFilter() { return priorityFilter; }

    /**
     * Sets priority value for filtering
     * @param priorityFilter
     */
    public void setPriorityFilter(int priorityFilter) { this.priorityFilter = priorityFilter; }


    /**
     * Gets start date for filtering
     * @return - long start date value
     */
    public long getStartDate() { return startDate; }

    /**
     * Sets start date for filtering
     * @param startDate - long start date value
     */
    public void setStartDate(long startDate) { this.startDate = startDate; }


    /**
     * Gets end date for filtering
     * @return - long end date value
     */
    public long getEndDate() { return endDate; }

    /**
     * Sets end date for filtering
     * @param endDate - long end date value
     */
    public void setEndDate(long endDate) { this.endDate = endDate; }

}


