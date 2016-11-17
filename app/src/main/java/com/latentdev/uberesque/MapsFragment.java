package com.latentdev.uberesque;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment {

    MapView mMapView;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {
                                    mMap = googleMap;
                                    //mMap.setPadding(0,findViewById(R.id.toolbar).getHeight(),0,0);
                                    //mMap.setPadding(0,(int)(56 * MapsFragment.super.getContext().getResources().getDisplayMetrics().density + 0.5f),0,0);

                                    /*if (ContextCompat.checkSelfPermission(MapsFragment.super.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                                            == PackageManager.PERMISSION_GRANTED) {
                                        mMap.setMyLocationEnabled(true);
                                    } else {
                                        ActivityCompat.requestPermissions(MapsFragment.super.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                                        if (ContextCompat.checkSelfPermission(MapsFragment.super.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                                                == PackageManager.PERMISSION_GRANTED) {
                                            mMap.setMyLocationEnabled(true);
                                        }
                                        else Toast.makeText(MapsFragment.super.getContext(),"permission required to run app",Toast.LENGTH_SHORT);
                                    }*/


                                    mMap.setMyLocationEnabled(true);

                                    LocationManager locationManager = (LocationManager) MapsFragment.super.getContext().getSystemService(LOCATION_SERVICE);
                                    Criteria criteria = new Criteria();
                                    String provider = locationManager.getBestProvider(criteria, true);
                                    Location location = locationManager.getLastKnownLocation(provider);

                                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(myLocation).title("location"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                                }
/*
        */


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


}
        );
        return rootView;
    }}
