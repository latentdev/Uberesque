package com.latentdev.uberesque;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laten on 12/1/2016.
 */

public class AsyncRides extends AsyncConnection {

    AsyncRides(Context in_context, Activity in_activity)
    {
        context=in_context;
        activity = in_activity;
        delegate = null;
    }

    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        if (delegate != null) {
            try {
                JSONObject jsonResponse = new JSONObject(result);
                if (jsonResponse.getBoolean("success") == true&&jsonResponse.getInt("state")==0) {
                    Response rides = new ResponseRides();
                    rides.rides=new ArrayList<>();
                    JSONArray array = jsonResponse.getJSONArray("rides");
                    for(int i=0;i<array.length();i++)
                    {
                        Ride ride = new Ride();
                        JSONObject object = array.getJSONObject(i);
                        ride.RideID = object.getInt("RideID");
                        ride.UserName = object.getString("User");
                        ride.Location = object.getString("Location");
                        ride.Location_Lat = object.getDouble("Location_Lat");
                        ride.Location_Long = object.getDouble("Location_Long");
                        ride.Destination = object.getString("Destination");
                        ride.Destination_Lat = object.getDouble("Destination_Lat");
                        ride.Destination_Long = object.getDouble("Destination_Long");
                        ride.Accepted = object.getBoolean("Accepted");
                        ride.Completed = object.getBoolean("Completed");
                        rides.rides.add(ride);
                    }
                    rides.success = jsonResponse.getBoolean("success");
                    rides.error = jsonResponse.getString("error");
                    delegate.postResult(rides);
                }
                else if(jsonResponse.getBoolean("success")==true&&jsonResponse.getInt("state")==1)
                {
                        Response accept = new Response();
                        accept.success = jsonResponse.getBoolean("success");
                        accept.error = jsonResponse.getString("error");
                    Toast.makeText(context, "Accepted Ride", Toast.LENGTH_LONG).show();
                        delegate.Reset();
                    }
                else
                    {
                        Toast.makeText(context, jsonResponse.getString("error"), Toast.LENGTH_LONG).show();
                    }

            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }

        } else {
            Log.e("ApiAccess", "You have not assigned IApiAccessResponse delegate");
        }
    }
}
