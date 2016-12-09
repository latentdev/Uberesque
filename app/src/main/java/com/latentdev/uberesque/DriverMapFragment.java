package com.latentdev.uberesque;


import android.*;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.latentdev.uberesque.R.id.container;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverMapFragment extends MapsFragment implements IAccessResponse{

    List<Ride> rides;

    LayoutInflater inflate;
    Dialog dialog;
    public DriverMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        inflate = inflater;
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        prepareMap();

        return rootView;
    }

    @Override
    public void prepareMap()
    {
        final MainActivity activity = (MainActivity) getActivity();

        String username = activity.user.UserName;
        String password = activity.user.Password;
        String url = "http://uberesque.azurewebsites.net/api/Account/Rides?username="+username+"&password="+password;
        final AsyncConnection async = new AsyncRides(this.getContext(),this.getActivity());
        async.delegate = this;
        async.execute(url);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;


                if (ContextCompat.checkSelfPermission(DriverMapFragment.super.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    ActivityCompat.requestPermissions(DriverMapFragment.super.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    if (ContextCompat.checkSelfPermission(DriverMapFragment.super.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    } else
                        Toast.makeText(DriverMapFragment.super.getContext(), "permission required to run app", Toast.LENGTH_SHORT);
                }

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View v = inflate.inflate(R.layout.info_window, null);

                        Iterator<Ride> rideIterator = rides.iterator();
                        while (rideIterator.hasNext()) {
                            Ride ride = rideIterator.next();
                            if (ride.MarkerID.equals(marker.getId().toString()))
                            {
                                TextView title = (TextView) v.findViewById(R.id.title);
                                title.setText(ride.UserName);
                                TextView location = (TextView) v.findViewById(R.id.location);
                                location.setText("Location: "+ride.Location);
                                TextView destination = (TextView) v.findViewById(R.id.destination);
                                destination.setText("Destination: "+ride.Destination);
                            }

                        }
                        return v;
                    }
                });
                if (mMap != null) {

                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(final Marker marker) {
                            final View dialogView = inflate.inflate(R.layout.dialog_driver, null);
                            dialog=new Dialog(getContext());
                            final Iterator<Ride> rideIterator = rides.iterator();
                            while (rideIterator.hasNext()) {
                                final Ride ride = rideIterator.next();
                                if (ride.MarkerID.equals(marker.getId().toString()))
                                {
                                    TextView title = (TextView) dialogView.findViewById(R.id.user);
                                    title.setText(ride.UserName);
                                    TextView location = (TextView) dialogView.findViewById(R.id.location);
                                    location.setText("Location: "+ride.Location);
                                    TextView destination = (TextView) dialogView.findViewById(R.id.destination);
                                    destination.setText("Destination: "+ride.Destination);
                                    Button submit = (Button) dialogView.findViewById(R.id.btn_submit);
                                    submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            EditText eta = (EditText) dialogView.findViewById(R.id.eta);
                                            int time = Integer.parseInt(eta.getText().toString());
                                            String url = "http://uberesque.azurewebsites.net/api/Account/AcceptRide?rideid="+ride.RideID+"&driverid="+activity.user.UserID+"&eta="+time;
                                            AsyncConnection async = new AsyncRides(getContext(),getActivity());
                                            async.delegate=DriverMapFragment.this;
                                            async.execute(url);
                                        }
                                    });
                                    dialog.setContentView(dialogView);
                                    dialog.show();

                                }

                            }
                        }
                    });
                }
                mMap.setMyLocationEnabled(true);
            }

        });

    }

    @Override
    public void postResult(Response asyncResult)
    {
        rides=new ArrayList<>();
        Iterator<Ride> rideIterator = asyncResult.rides.iterator();

        while (rideIterator.hasNext()) {
            final Ride ride=rideIterator.next();
            LatLng myLocation = new LatLng(ride.Location_Lat, ride.Location_Long);
            ride.MarkerID = mMap.addMarker(new MarkerOptions().position(myLocation)).getId();
            rides.add(ride);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(myLocation));

        }

    }

    @Override
    public void Reset()
    {
        dialog.hide();

    }

}
