package com.diegoalvis.android.happywish.adapters;

import android.view.View;

import com.diegoalvis.android.happywish.models.Application;
import com.diegoalvis.android.happywish.models.Category;

/**
 * Created by diegoalvis on 1/26/17.
 */

// Interface when tap item of list
public interface OnItemClickListener {
    void onItemClickApp(Application application, AppAdapter.AppViewHolder holder);
    void onItemClickCategory(Category category);
}
