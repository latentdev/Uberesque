package com.latentdev.uberesque;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by Laten on 12/4/2016.
 */

public class AsyncRequestRide extends AsyncConnection {


    AsyncRequestRide(Context in_context, Activity in_activity)
    {
        context=in_context;
        activity = in_activity;
        delegate = null;
    }

    protected void onPostExecute(String result) {
        //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        if (delegate != null) {
            try {
                JSONObject jsonResponse = new JSONObject(result);
                if (jsonResponse.getBoolean("success") == true) {
                    Toast.makeText(context, "success", Toast.LENGTH_LONG).show();

                }
                else if (jsonResponse.getBoolean("success") == false)
                {
                    Toast.makeText(context, "failed: "+ jsonResponse.getString("error").toString(), Toast.LENGTH_LONG).show();
                }
            }
                catch (Exception e)
                {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        else
        {
            Toast.makeText(context, "You have not assigned IApiAccessResponse delegate", Toast.LENGTH_LONG).show();
        }
}}
