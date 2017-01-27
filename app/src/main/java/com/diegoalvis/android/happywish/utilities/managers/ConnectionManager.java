package com.diegoalvis.android.happywish.utilities.managers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by Diego on 12/09/2016.
 */
public abstract class ConnectionManager {


    public static final boolean checkInternetAccess(Context context){

        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State stateMobile = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State stateWifi = conManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        if (stateMobile == NetworkInfo.State.CONNECTED || stateWifi == NetworkInfo.State.CONNECTED){
            return true;
        } else {
            return false;
        }
    }

}
