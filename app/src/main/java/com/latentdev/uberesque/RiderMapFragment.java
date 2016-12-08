package com.latentdev.uberesque;

import android.app.Dialog;
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

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

/**
 * A simple {@link Fragment} subclass.
 */
public class RiderMapFragment extends MapsFragment{

public FloatingActionButton fab;
    EditText location;
    EditText destination;
    Button submit;


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
                        string current = location.getText();
                        string dest =  destination.getText();

                        string url = "http://uberesque.azurewebsites.net/api/Account/RequestRide";
                         dialog.hide();
                    }
                                          }


               /* Snackbar.make(v, "Hello", Snackbar. LENGTH_LONG).setAction("Action", null).show();*/

            }
        });
        return v;
    }

}



