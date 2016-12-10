package com.latentdev.uberesque;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import org.json.JSONObject;

public class RiderMapFragment extends MapsFragment{

    public FloatingActionButton fab;
    EditText location;
    EditText destination;
    Button submit;
    Context context;
    User User;
    IAccessResponse delegate;
    IAccessResponse delegate2;


    public RiderMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ridermap, container, false);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        final Dialog dialog = new Dialog(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_writer, container, false);
        dialog.setContentView(dialogView);
        location = (EditText) dialogView.findViewById(R.id.current);
        destination = (EditText) dialogView.findViewById(R.id.desired);
        submit = (Button) dialogView.findViewById(R.id.btn_submit);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        prepareMap();


        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                dialog.show();
                submit.setOnClickListener(new View.OnClickListener(){
                                              @Override
                                              public void onClick(View v){
                                                  boolean valid = true;
                                                  String current = location.getText().toString();
                                                  String dest =  destination.getText().toString();

                                                  if (current.isEmpty() || current.length() < 4 ) {
                                                      current.setError("enter a valid address");
                                                      valid = false;
                                                  } else {
                                                      current.setError(null);
                                                  }
                                                  if (dest.isEmpty() || dest.length() < 4 ) {
                                                      dest.setError("invalid destination");
                                                      valid = false;
                                                  } else {
                                                      dest.setError(null);
                                                  }
                                                 String result =  "https://maps.googleapis.com/maps/api/geocode/json?address=current= AIzaSyCQRMoffAyYYI54fB6AcjQIq4tzSgjoNSE";
                                                  String result2 =  "https://maps.googleapis.com/maps/api/geocode/json?address=dest= AIzaSyCQRMoffAyYYI54fB6AcjQIq4tzSgjoNSE";

                                                  if(delegate!=null) {
                                                      try {
                                                          RiderResponse r = new RiderResponse();
                                                          JSONObject obj = new JSONObject(result);
                                                          if (obj.getString("status").equals("OK")) {
                                                              JSONObject loc = res.getJSONObject("geometry").getJSONObject("location");
                                                              r.latitude = loc.getDouble("lat");
                                                              r.longitude = loc.getDouble("lng");

                                                              //Toast.makeText(context, "success", Toast.LENGTH_LONG).show();
                                                              //  delegate.postResult(RiderResponse);
                                                          }
                                                          } else {
                                                              Toast.makeText(context, loc.getString("error"), Toast.LENGTH_LONG).show();
                                                              delegate.Reset();
                                                          }
                                                      } catch (Exception e) {
                                                          Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                                                      }
                                                  }
                                              if(delegate2!=null) {
                                                  try {
                                                      RiderResponse r2 = new RiderResponse();
                                                      JSONObject obj2 = new JSONObject(result2);
                                                      if (obj2.getString("status").equals("OK")) {
                                                          JSONObject loc2 = res.getJSONObject("geometry").getJSONObject("location");
                                                          r.latitude = loc2.getDouble("lat");
                                                          r.longitude = loc2.getDouble("lng");

                                                          // Toast.makeText(context, "success", Toast.LENGTH_LONG).show();
                                                          //  delegate.postResult(RiderResponse);
                                                      }
                                                  }else {
                                                     Toast.makeText(context, loc.getString("error"), Toast.LENGTH_LONG).show();
                                                      delegate.Reset();
                                                  }
                                              } catch(Exception e) {
                                                  Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                                              }
                                          }
                                                  String url = "http://uberesque.azurewebsites.net/api/Account/RequestRide?:UserID="+User.UserID+"&User"+User.Username+"Location&" + current+"&Location_Lat="+r.latitude +"&Location_Long"+r.longitude+"&Destination"+dest+"&Destination_Lat"+r2.latitude+"&Destination_Long"+r2.longitutude;
                                                  AsyncRequestRide async = new AsyncRequestRide(this.getContext(),this.getActivity());
                                                  async.delegate = this;
                                                  async.execute(url);
                                                  submit.setEnabled(true);
                                                  dialog.hide();
                                                  FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                  ft.replace(R.id.mapView, fragment);
                                                  ft.commit();
                                              }
                                          }
            }
            return v;
            }


