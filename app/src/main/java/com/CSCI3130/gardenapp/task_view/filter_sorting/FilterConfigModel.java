package com.CSCI3130.gardenapp.task_view.filter_sorting;

import com.google.firebase.database.annotations.NotNull;

import java.io.Serializable;

/**
 * Model class containing all the different filter configs
 *
 * @author Liam Hebert
 * @see com.CSCI3130.gardenapp.db.DatabaseTaskOrderer
 */
public class FilterConfigModel implements Serializable {

    @NotNull
    int priorityFilter;
    @NotNull
    long startDate;
    @NotNull
    long endDate;

    /**
     * Initializes a FilterConfigModel to filter tasks
     *
     * @param priorityFilter priority value to show in the database
     * @param startDate      minimum date for a task in millis
     * @param endDate        maximum date for a task in millis
     */
    public FilterConfigModel(int priorityFilter, long startDate, long endDate) {
        this.priorityFilter = priorityFilter;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Initializes a FilterConfigModel that filters no tasks
     */
    public FilterConfigModel() {
        this.priorityFilter = -1;
        this.startDate = 0;
        this.endDate = Long.MAX_VALUE;
    }

    /**
     * Returns the current filtered priorityFilter. -1 if no filter
     *
     * @return current priority filter
     */
    public int getPriorityFilter() {
        return priorityFilter;
    }

    /**
     * Sets the current priority filter value. Must be between 1 and 5 or -1 for none
     *
     * @param priorityFilter priority filter value to place
     */
    public void setPriorityFilter(int priorityFilter) {
        this.priorityFilter = priorityFilter;
    }

    /**
     * Returns the minimum date for a task in millis
     *
     * @return the minimum date for a task
     */
    public long getStartDate() {
        return startDate;
    }

    /**
     * Sets the minimum date for at task in millis. 0 for no minimum
     *
     * @param startDate minimum date for a task
     */
    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the maximum date for a task
     *
     * @return the maximum date for a task
     */
    public long getEndDate() {
        return endDate;
    }

    /**
     * Sets the maximum date for a task in millis, Long.MAX_VALUE if no max
     *
     * @param endDate the maximum date for a class in millis
     */
    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
}
