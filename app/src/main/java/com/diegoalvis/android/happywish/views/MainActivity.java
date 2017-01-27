package com.diegoalvis.android.happywish.views;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diegoalvis on 1/23/17.
 */

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int ID_CATEGORY_ALL = -1;
    private static final int NO_INTERNET = 0;
    private static final int REQUEST_FAIL = 1;
    private ListAppFragment listAppFragment;
    private ListCatFragment listCatFragment;

    private List<Category> categories;
    private List<Application> applications;
    private List<Application> filteredApps;

    private CoordinatorLayout coordinator;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout.LayoutParams params;

    public boolean tabletSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get type of device
        tabletSize = getResources().getBoolean(R.bool.isTablet);
        // set Orientation
        if(tabletSize)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // get swipe if is smartphone
            swipeRefreshLayout  = (SwipeRefreshLayout) findViewById(R.id.swipe_main);
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
            swipeRefreshLayout.setOnRefreshListener(this);
        }

        // get views
        coordinator         = (CoordinatorLayout) findViewById(R.id.main_content);
        progressBar         = new ProgressBar(this);
        params              = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity      = (Gravity.CENTER);

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
        filteredApps = new ArrayList<>();
        listAppFragment = new ListAppFragment();
        listAppFragment.setData(filteredApps);

        // load fragment view
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_content_fragment, listCatFragment);
        if(tabletSize)
            fragmentTransaction.replace(R.id.secondary_content_fragment, listAppFragment);
        fragmentTransaction.commit();

        getData();
    }


    private void getData() {
        // check internet access
        if(ConnectionManager.checkInternetAccess(this)) {
            // get data of web service
            doRequest();
        } else {
            // show message NO Internet
            showSnackBar(getString(R.string.msg_no_internet), getString(R.string.retry_action));
            // get data of cache memory
            try {
                // Retrieve the list from internal storage
                loadApps((List<Application>) CacheStorage.readObject(this));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // Show message when there isn't internet or the request fail
    private void showSnackBar(String erromMsg, String actionMsg) {
        Snackbar.make(coordinator, erromMsg, Snackbar.LENGTH_INDEFINITE).setAction(actionMsg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        }).show();
    }

    private void doRequest() {
        // show progress bar while do request
        coordinator.addView(progressBar, params);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiInterface.getApplications();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // remove progress bar
                coordinator.removeView(progressBar);
                try {
                    loadApps(WebResponseManager.parseFromJSON(response.body()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // remove progress bar
                coordinator.removeView(progressBar);
                // show message Request fail
                showSnackBar(getString(R.string.msg_request_fail), getString(R.string.reload_action));
                // get data of cache memory
                try {
                    // Retrieve the list from internal storage
                    loadApps((List<Application>) CacheStorage.readObject(MainActivity.this));
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
            CacheStorage.writeObject(this, applications);
        } catch (IOException e) {
            Log.e("Alvis", e.getMessage());
        }
    }

    public void onSelectedCategory(int categoryId, String categoryName){
        // set application of category selected
        filteredApps.clear();
        for(Application app : applications) {
            if(categoryId == ID_CATEGORY_ALL || categoryId == app.getCategory().getId()) {
                filteredApps.add(app);
            }
        }

        listAppFragment.setData(filteredApps);
        listAppFragment.setCategoryName(categoryName);
        listAppFragment.notifyData();

        if(!tabletSize) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.main_content_fragment, listAppFragment).commit();
        }

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

    @Override
    public void onRefresh() {
        getData();
        // hide refresh
        if(swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }
}
