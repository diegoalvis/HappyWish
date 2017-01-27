package com.diegoalvis.android.happywish.views.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diegoalvis.android.happywish.R;
import com.diegoalvis.android.happywish.adapters.CategoryAdapter;
import com.diegoalvis.android.happywish.adapters.OnItemClickListener;
import com.diegoalvis.android.happywish.models.Application;
import com.diegoalvis.android.happywish.models.Category;
import com.diegoalvis.android.happywish.views.MainActivity;

import java.util.List;

/**
 * Created by diegoalvis on 1/24/17.
 */

public class ListCatFragment extends Fragment implements OnItemClickListener {

    RecyclerView recyclerView;

    private List<Category> categories;
    private CategoryAdapter adapter;

    public ListCatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_cat, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new CategoryAdapter(getActivity(), categories, ListCatFragment.this);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        recyclerView.setAdapter(adapter);

        return v;
    }


    public void setData(List<Category> categories) {
        this.categories = categories;
    }

    public void notifyData(){
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClickApp(Application application) {
        // Shouldn't be implemented in this scope
    }

    @Override
    public void onItemClickCategory(Category category) {
        ((MainActivity) getActivity()).onSelectedCategory(category.getId(), category.getCategory());
    }
}
