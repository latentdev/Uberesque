package com.latentdev.uberesque;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Laten on 11/26/2016.
 */


    public class AsyncConnection extends AsyncTask<String, Void, String> {

        Context context;
        Activity activity;
        Response response;
        IAccessResponse delegate;

        AsyncConnection()
        {

        }

        AsyncConnection(Context in_context, Activity in_activity)
        {
            context=in_context;
            activity = in_activity;
            delegate = null;
        }

        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0],"GET",null);
            } catch (IOException e) {
                return "Unable to connect. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            if(delegate!=null)
            {
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    if (jsonResponse.getBoolean("success")==true) {
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
                        Toast.makeText(context, "success", Toast.LENGTH_LONG).show();
                        delegate.postResult(response);
                    }
                    else
                    {
                        Toast.makeText(context, jsonResponse.getString("error"), Toast.LENGTH_LONG).show();
                        delegate.Reset();
                    }
                }catch(Exception e)
                {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }

            }
            else
            {
                Log.e("ApiAccess", "You have not assigned IApiAccessResponse delegate");
            }
        }


    public String downloadUrl(String myurl, String method, String json) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setAllowUserInteraction(false);

            if (json != null) {
                //set the content length of the body
                conn.setFixedLengthStreamingMode(json.getBytes().length);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);

                //send the json as body of the request
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(json.getBytes("UTF-8"));
                outputStream.close();
            }
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            //Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a JSONObject
            return readIt(is);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {

        BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);
        return responseStrBuilder.toString();
    }

}

