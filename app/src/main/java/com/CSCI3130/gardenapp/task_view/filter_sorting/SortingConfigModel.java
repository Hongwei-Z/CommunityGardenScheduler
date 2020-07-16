package com.CSCI3130.gardenapp.task_view.filter_sorting;

import com.google.firebase.database.annotations.NotNull;

import java.io.Serializable;

/**
 * Model class containing all the different Sorting configs
 *
 * @author Liam Hebert
 * @see com.CSCI3130.gardenapp.db.DatabaseTaskOrderer
 */
public class SortingConfigModel implements Serializable {
    @NotNull
    SortCategory sortCat;
    @NotNull
    SortOrder sortOrder;

    /**
     * Initializes a SortingConfigModel to filter tasks
     *
     * @param sortCat   SortingCategory to sort tasks on, NONE if no sort
     * @param sortOrder SortingOrder to order tasks on, NONE if no order
     */
    public SortingConfigModel(SortCategory sortCat, SortOrder sortOrder) {
        this.sortCat = sortCat;
        this.sortOrder = sortOrder;
    }

    /**
     * Default constructor for a SortingConfigModel. Sets sortCat = None and sortOrder = None
     */
    public SortingConfigModel() {
        this.sortCat = SortCategory.NONE;
        this.sortOrder = SortOrder.NONE;
    }

    /**
     * Returns the current sorting category
     *
     * @return the current sorting category
     */
    public SortCategory getSortCat() {
        return sortCat;
    }

    /**
     * Set the sorting category to use.
     *
     * @param sortCat SortingCategory to replace the current one.
     */
    public void setSortCat(SortCategory sortCat) {
        this.sortCat = sortCat;
    }

    /**
     * Returns the sorting order currently use. NONE if using context-specific order
     *
     * @return The current sorting order
     */
    public SortOrder getSortOrder() {
        return sortOrder;
    }

    /**
     * Sets the sorting order to use. NONE if you want to use the context-specific default
     *
     * @param sortOrder the sorting order to use
     */
    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }
}
