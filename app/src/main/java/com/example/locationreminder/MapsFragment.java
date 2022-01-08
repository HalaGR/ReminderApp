package com.example.locationreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import android.widget.Button;
public class MapsFragment extends Fragment {


    @Override
    public View onCreateView( LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_maps, container, false);
        //Initialize map fragment
        SupportMapFragment suupportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        //Async map
        suupportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady( GoogleMap googleMap) {
                   //when map loaded
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        //when clcked on map
                        //Initialize marker options
                        MarkerOptions markerOptions=new MarkerOptions();
                        //Set posotion of marker
                        markerOptions.position(latLng);
                        //Set title of marker
                        markerOptions.title(latLng.latitude+":"+latLng.longitude);
                        //Remove all marker
                        googleMap.clear();
                        //animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                latLng,10
                        ));
                        //add marker on map
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });

        //Return view
        return view;
    }


}