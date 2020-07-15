package com.CSCI3130.gardenapp.db;

import com.CSCI3130.gardenapp.task_view.ActiveTaskListContext;
import com.CSCI3130.gardenapp.task_view.filter_sorting.FilterConfigModel;
import com.CSCI3130.gardenapp.task_view.filter_sorting.SortCategory;
import com.CSCI3130.gardenapp.task_view.filter_sorting.SortOrder;
import com.CSCI3130.gardenapp.task_view.filter_sorting.SortingConfigModel;
import com.CSCI3130.gardenapp.util.data.CurrentWeather;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Utility Class for TaskDatabase to filter and sort tasks in the correct order based on user settings
 *
 * @author Liam Hebert
 */
class DatabaseTaskOrderer {
    /**
     * Entry point for the filtering and sorting
     *
     * @param tasks                 Tasks to sort and filter
     * @param filterConfigModel     Filter settings to configure filtering
     * @param sortingConfigModel    Sorting settings to configure filtering
     * @param activeTaskListContext Context of the task list to further configure sorting and filtering
     * @return Sorted and Filtered task list
     */
    static ArrayList<Task> filterSortTasks(ArrayList<Task> tasks,
                                           FilterConfigModel filterConfigModel,
                                           SortingConfigModel sortingConfigModel,
                                           ActiveTaskListContext activeTaskListContext) {
        ArrayList<Task> newTasks = new ArrayList<>();
        //Filter tasks based on user settings
        filterTasksOnSetting(tasks, newTasks, filterConfigModel);
        //sort them on user settings
        sortTasks(newTasks, sortingConfigModel, activeTaskListContext);
        //filter based on current weather
        filterWeather(newTasks);

        return newTasks;
    }

    private static void filterWeather(ArrayList<Task> tasks) {
        //move weather tasks to the end of the queue based on whether they match with the current weather
        ArrayList<Task> result = new ArrayList<>(tasks);
        for (Task curr : result) {
            //current task has a weather trigger
            WeatherCondition trigger = curr.getWeatherTrigger();
            if (trigger != WeatherCondition.NONE) {
                //if this task's weather trigger does not match those of the current weather conditions, throw it down to the bottom of the queue
                ArrayList<WeatherCondition> currList = CurrentWeather.currentWeatherList;
                if (currList.contains(trigger)) {
                    tasks.remove(curr);
                    tasks.add(0, curr);
                } else {
                    tasks.remove(curr);
                    tasks.add(tasks.size(), curr);
                }
            }
        }
    }

    private static void sortTasks(ArrayList<Task> tasks, SortingConfigModel sortingConfigModel, ActiveTaskListContext activeTaskListContext) {
        //if there's a sort category and order, sort accordingly
        if (sortingConfigModel.getSortCat() != SortCategory.NONE && sortingConfigModel.getSortOrder() != SortOrder.NONE) {
            Comparator<Task> comparator;
            switch (sortingConfigModel.getSortCat()) {
                case PRIORITY:
                    comparator = Comparator.comparingLong(Task::getPriority);
                    break;
                case AZ:
                    comparator = Comparator.comparing(Task::getName);
                    break;
                default:
                    if (activeTaskListContext.equals(ActiveTaskListContext.TASK_HISTORY))
                        comparator = Comparator.comparingLong(Task::getDateCompleted);
                    else
                        comparator = Comparator.comparingLong(Task::getDateDue);
                    break;
            }
            tasks.sort(comparator);
            //sort in descending order, if applicable
            if (sortingConfigModel.getSortOrder() == SortOrder.DESCENDING) {
                Collections.reverse(tasks); //since sorting is already done in descending order
            }
        }
    }

    private static void filterTasksOnSetting(ArrayList<Task> currentTasks, ArrayList<Task> newTasks, FilterConfigModel filterConfigModel) {
        //filter tasks
        for (Task task : currentTasks) {

            if ((task.getDateDue() >= filterConfigModel.getStartDate() && task.getDateDue() <= filterConfigModel.getEndDate()) || task.getWeatherTrigger() != WeatherCondition.NONE) {
                if (filterConfigModel.getPriorityFilter() != -1) {
                    if (filterConfigModel.getPriorityFilter() == task.getPriority()) {
                        newTasks.add(task);
                    }
                } else {
                    newTasks.add(task);
                }
            }
        }
    }
}
