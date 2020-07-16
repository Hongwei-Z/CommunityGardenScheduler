package com.CSCI3130.gardenapp.task_view.task_view_list;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.Welcome;
import com.CSCI3130.gardenapp.create_task.CreateTaskActivity;
import com.CSCI3130.gardenapp.db.TaskDatabase;
import com.CSCI3130.gardenapp.task_view.ActiveTaskListContext;
import com.CSCI3130.gardenapp.task_view.filter_sorting.FilterConfigModel;
import com.CSCI3130.gardenapp.task_view.filter_sorting.FilterPopUp;
import com.CSCI3130.gardenapp.task_view.filter_sorting.SortOrder;
import com.CSCI3130.gardenapp.task_view.filter_sorting.SortingConfigModel;
import com.google.android.material.snackbar.Snackbar;


/**
 * Activity class to process the task data and populate the task list
 *
 * @author Elizabeth Eddy and Logan Sutherland
 */
public class TaskViewList extends AppCompatActivity {

    public static ActiveTaskListContext activeTaskListContext;
    private static Context mContext;
    protected RecyclerView recyclerView;
    TaskDatabase db;
    Dialog myDialog;
    SortOrder sortOrderDefault;
    SortingConfigModel sortingConfigModel;
    SortingConfigModel lastSortingConfigModel;
    FilterConfigModel filterConfigModel;
    FilterConfigModel lastFilterConfigModel;
    private ActiveTaskListContext lastContext = ActiveTaskListContext.ALL_TASKS; //default to all tasks
    private RecyclerView.LayoutManager layoutManager;

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

    FilterConfigModel getFilterConfigModel() {
        return filterConfigModel;
    }

    SortingConfigModel getSortingConfigModel() {
        return sortingConfigModel;
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
        sortOrderDefault = SortOrder.ASCENDING;
        // initializing fallback history

        if (lastFilterConfigModel == null)
            lastFilterConfigModel = new FilterConfigModel();
        if (lastSortingConfigModel == null) {
            lastSortingConfigModel = new SortingConfigModel();
            lastSortingConfigModel.setSortOrder(sortOrderDefault);
        }

        activeTaskListContext = (ActiveTaskListContext) getIntent().getSerializableExtra("activeTaskListContext");
        if (activeTaskListContext == null)
            activeTaskListContext = lastContext;
        lastContext = activeTaskListContext;
        configureActiveTaskListContext();

        configureSortingAndFiltering();

        db = new TaskDatabase();
        db.setDbRead(activeTaskListContext);

        recyclerView = findViewById(R.id.recycleview_tasks);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        db.getDbRead().addValueEventListener(
                db.getTaskData(recyclerView, activeTaskListContext, sortingConfigModel, filterConfigModel));
    }

    private void configureActiveTaskListContext() {
        TextView toolbarTitle = findViewById(R.id.page_name);
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
    }

    private void configureSortingAndFiltering() {


        sortingConfigModel = (SortingConfigModel) getIntent().getSerializableExtra(getString(R.string.sortingConfig));
        filterConfigModel = (FilterConfigModel) getIntent().getSerializableExtra(getString(R.string.filteringConfig));

        if (sortingConfigModel != null) {
            if (sortingConfigModel.getSortOrder() == SortOrder.NONE) {
                sortingConfigModel.setSortOrder(sortOrderDefault);
            }
        } else
            sortingConfigModel = lastSortingConfigModel;

        if (filterConfigModel == null)
            filterConfigModel = lastFilterConfigModel;
        lastSortingConfigModel = sortingConfigModel;
        lastFilterConfigModel = filterConfigModel;
    }

    /**
     * Method opens popup with filters and sorting options
     *
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, Welcome.class);
        startActivity(i);
    }

}


