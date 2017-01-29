package com.diegoalvis.android.happywish.views;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.diegoalvis.android.happywish.R;
import com.diegoalvis.android.happywish.models.Application;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by diegoalvis on 1/23/17.
 */

public class ResumeAppActivity extends AppCompatActivity {

    public static final String SELECT_APP_KEY = "select app";

    private String name, link;

    private TextView titleApp, priceApp, authorApp, summaryApp, urlApp;
    private ImageView imageApp;
    private Toolbar toolbar;

    private boolean tabletSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_app);

        // get selected application of intent extras
        Application application = (Application) getIntent().getSerializableExtra(SELECT_APP_KEY);
        // set global variables values
        name = application.getName();
        link = application.getLink();


        setOrientation();
        getViews();
        setupToolbar();
        loadData(application);


    }

    private void setOrientation() {
        // get type of device
        tabletSize = getResources().getBoolean(R.bool.isTablet);
        // set Orientation
        if (tabletSize) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            CollapsingToolbarLayout collapser = (CollapsingToolbarLayout) findViewById(R.id.collapser);
            collapser.setTitle(name); // Change title
        }
    }

    private void getViews() {
        titleApp    = (TextView) findViewById(R.id.app_title_resume);
        priceApp    = (TextView) findViewById(R.id.app_price_resume);
        authorApp   = (TextView) findViewById(R.id.app_author_resume);
        summaryApp  = (TextView) findViewById(R.id.app_summary_resume);
        urlApp      = (TextView) findViewById(R.id.app_link_resume);
        imageApp    = (ImageView) findViewById(R.id.app_image_resume);
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(name); // Change title
    }



    private void loadData(final Application application) {
        // set app info in views
        titleApp.setText(application.getTitle());
        authorApp.setText(application.getRights());
        summaryApp.setText(application.getSummary());

        // set app price
        priceApp.setText("$ " + NumberFormat.getNumberInstance(Locale.US).format(application.getPrice()));
        if(application.getPrice() == 0.0)
            priceApp.setText(R.string.free_label);

        // set app URL
        String htmlLink = String.format(getString(R.string.footer_label_url_app) + " <a href='%s'>" + getString(R.string.here_label) + "</a>", application.getLink());
        urlApp.setText(Html.fromHtml(htmlLink));
        urlApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(application.getLink()));
                startActivity(intent);
            }
        });

        // set app image
        try {
            Picasso.with(this).load(application.getImage()).into(imageApp);
        } catch (Exception e) {
            Log.e("ALVIS", "Error al cargar la iamgen");
        }
    }



    //region Option Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_resume_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;

            // Respond to the action bar's share button
            case R.id.action_share:
                Intent i=new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_SUBJECT, name);
                i.putExtra(android.content.Intent.EXTRA_TEXT, link);
                startActivity(Intent.createChooser(i, "Share URL"));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion
}
