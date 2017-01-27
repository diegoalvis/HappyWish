package com.diegoalvis.android.happywish.networking;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;


/**
 * Created by diegoalvis on 1/23/17.
 *
 * Interface where define all
 * request with their repective
 * fieds for the application
 */

public interface ApiInterface {


    @Headers({"Content-Type: application/json"})
    @GET("/us/rss/topfreeapplications/limit=20/json")
    Call<JsonObject> getApplications();

}
