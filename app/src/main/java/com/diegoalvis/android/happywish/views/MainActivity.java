package com.diegoalvis.android.happywish.views;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.diegoalvis.android.happywish.R;
import com.diegoalvis.android.happywish.models.Application;
import com.diegoalvis.android.happywish.models.Category;
import com.diegoalvis.android.happywish.networking.ApiClient;
import com.diegoalvis.android.happywish.networking.ApiInterface;
import com.diegoalvis.android.happywish.utilities.classes.CacheStorage;
import com.diegoalvis.android.happywish.utilities.managers.ConnectionManager;
import com.diegoalvis.android.happywish.utilities.managers.WebResponseManager;
import com.diegoalvis.android.happywish.views.fragments.ListAppFragment;
import com.diegoalvis.android.happywish.views.fragments.ListCatFragment;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diegoalvis on 1/23/17.
 */

public class MainActivity extends AppCompatActivity {

    private static final int ID_CATEGORY_ALL = -1;
    private ListAppFragment listAppFragment;
    private ListCatFragment listCatFragment;

    private List<Application> applications;
    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get views

        // set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        // set content fragments
        loadFragments();

    }


    private void loadFragments() {
        // Initialize fragment categories
        categories = new ArrayList<>();
        listCatFragment = new ListCatFragment();
        listCatFragment.setData(categories);

        // Initialize fragment applications
        applications = new ArrayList<>();
        listAppFragment = new ListAppFragment();
        listAppFragment.setData(applications);

        // load fragment view
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_content_fragment, listCatFragment);
        fragmentTransaction.commit();

        getData();
    }


    private void getData() {
        // check internet access
        if(ConnectionManager.checkInternetAccess(this)) {
            // get data of web service
            doRequest();
        } else {
            // get data of cache memory
            try {
                // Retrieve the list from internal storage
                loadApps((List<Application>) CacheStorage.readObject(this, getString(R.string.app_name)));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void doRequest() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiInterface.getApplications();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    loadApps(WebResponseManager.parseFromJSON(response.body()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // get data of cache memory
                try {
                    // Retrieve the list from internal storage
                    loadApps((List<Application>) CacheStorage.readObject(MainActivity.this, getString(R.string.app_name)));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void loadApps(List<Application> applications) {
        this.applications = applications;
        if (applications.size() > 0) {
            categories.clear();
            // Create All category
            Category allCat = new Category(ID_CATEGORY_ALL, "ALL", ApiClient.BASE_URL);
            categories.add(allCat);
            List<Integer> categoryIds = new ArrayList<>();
            for (Application application : applications) {
                if (!categoryIds.contains(application.getCategory().getId())) {
                    categories.add(application.getCategory());
                    categoryIds.add(application.getCategory().getId());
                }

            }
            listCatFragment.notifyData();
        }

        // Cache data
        saveApps();
    }


    private void saveApps() {
        try {
            // Save the list of entries to internal storage
            CacheStorage.writeObject(this, getString(R.string.app_name), applications);
        } catch (IOException e) {
            Log.e("Alvis", e.getMessage());
        }
    }

    public void onSelectedCategory(int categoryId, String categoryName){
        // set application of category selected
        List<Application> filteredApps = new ArrayList<>();
        for(Application app : applications) {
            if(categoryId == ID_CATEGORY_ALL || categoryId == app.getCategory().getId()) {
                filteredApps.add(app);
            }
        }

        listAppFragment.setData(filteredApps);
        listAppFragment.setCategoryName(categoryName);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_content_fragment, listAppFragment).commit();

    }

    @Override
    public void onBackPressed() {
        if(listAppFragment.isVisible()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.main_content_fragment, listCatFragment).commit();
        }
        else {
            finish();
        }
    }
}
