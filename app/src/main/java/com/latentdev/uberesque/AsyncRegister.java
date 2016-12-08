package com.latentdev.uberesque;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Laten on 12/6/2016.
 */

public class AsyncRegister extends AsyncConnection {

    Response response;
    AsyncRegister(Context in_context, Activity in_activity, Response in_response)
    {
        context=in_context;
        activity = in_activity;
        delegate = null;
        response = in_response;
    }

    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            /*
            JSONObject jsonInfo = new JSONObject();
                jsonInfo.put("username", response.user.UserName);
                jsonInfo.put("password", response.user.Password);
                jsonInfo.put("email", response.user.Email);
                jsonInfo.put("firstname", response.user.FirstName);
                jsonInfo.put("lastname", response.user.LastName);
                jsonInfo.put("driver", response.user.Driver);
                if (response.user.Driver) {
                    jsonInfo.put("color", response.vehicle.Color);
                    jsonInfo.put("year", response.vehicle.Year);
                    jsonInfo.put("make", response.vehicle.Make);
                    jsonInfo.put("model", response.vehicle.Model);
                }
*/
            return downloadUrl(urls[0],"GET",null);
        } catch (IOException e) {
            Log.e("JSON",e.toString());
            return "Unable to connect. URL may be invalid.";
        }/*catch (JSONException json)
        {
            Log.e("JSON",json.toString());
            return "Problem with JSON";
        }*/
    }

    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        if (delegate != null) {
            try {
                Response response;
                JSONObject jsonResponse = new JSONObject(result);
                if (jsonResponse.getBoolean("success") == true) {
                    Toast.makeText(context, "success", Toast.LENGTH_LONG).show();
                    response = new Response();
                    response.user.UserID = jsonResponse.getJSONObject("user").getInt("UserID");
                    response.user.Email = jsonResponse.getJSONObject("user").getString("Email");
                    response.user.UserName = jsonResponse.getJSONObject("user").getString("UserName");
                    response.user.Password = jsonResponse.getJSONObject("user").getString("Password");
                    response.user.FirstName = jsonResponse.getJSONObject("user").getString("FirstName");
                    response.user.LastName = jsonResponse.getJSONObject("user").getString("LastName");
                    response.user.Driver = jsonResponse.getJSONObject("user").getBoolean("Driver");
                    if (response.user.Driver == true) {
                        response.vehicle.Color = jsonResponse.getJSONObject("vehicle").getString("Color");
                        response.vehicle.Year = jsonResponse.getJSONObject("vehicle").getInt("Year");
                        response.vehicle.Make = jsonResponse.getJSONObject("vehicle").getString("Make");
                        response.vehicle.Model = jsonResponse.getJSONObject("vehicle").getString("Model");
                        response.vehicle.Plate = jsonResponse.getJSONObject("vehicle").getString("Plate");
                    }
                    delegate.postResult(response);
                }
                else if (jsonResponse.getBoolean("success") == false)
                {
                    Toast.makeText(context, "failed: "+ jsonResponse.getString("error").toString(), Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                Log.e("JSON",e.toString());
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(context, "You have not assigned IApiAccessResponse delegate", Toast.LENGTH_LONG).show();
        }


    }

}

