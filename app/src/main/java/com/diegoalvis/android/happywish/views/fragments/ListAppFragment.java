package com.diegoalvis.android.happywish.views.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diegoalvis.android.happywish.R;
import com.diegoalvis.android.happywish.adapters.AppAdapter;
import com.diegoalvis.android.happywish.adapters.OnItemClickListener;
import com.diegoalvis.android.happywish.models.Application;
import com.diegoalvis.android.happywish.models.Category;
import com.diegoalvis.android.happywish.views.MainActivity;
import com.diegoalvis.android.happywish.views.ResumeAppActivity;

import java.util.List;

/**
 * Created by diegoalvis on 1/25/17.
 */

public class ListAppFragment extends Fragment implements OnItemClickListener {

    RecyclerView recyclerView;
    TextView categoryLabel;

    private List<Application> applications;
    private AppAdapter adapter;
    private String categoryName = "ALL";

    public ListAppFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_app, container, false);

        RecyclerView.LayoutManager layoutManager;
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_list);
        recyclerView.setHasFixedSize(false);
        // set view in Grid if is tablet
        if(((MainActivity) getActivity()).tabletSize)
            layoutManager = new GridLayoutManager(getActivity(), 2);
        else
            layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        adapter = new AppAdapter(getActivity(), applications, this);
        recyclerView.setAdapter(adapter);

        categoryLabel = (TextView) v.findViewById(R.id.category_label);
        categoryLabel.setText(categoryName);

        return v;
    }


    public void setData(List<Application> applications) {
        this.applications = applications;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void notifyData(){
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClickApp(Application application) {
        Intent intent = new Intent(getActivity(), ResumeAppActivity.class);
        intent.putExtra(ResumeAppActivity.SELECT_APP_KEY, application);
        startActivity(intent);
    }

    @Override
    public void onItemClickCategory(Category category) {
        // Shouldn't be implemented in this scope
    }
}
