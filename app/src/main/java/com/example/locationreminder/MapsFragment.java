package com.example.locationreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import android.widget.Button;
//************************
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import kotlin.jvm.internal.Intrinsics;

public class MapsFragment extends Fragment {
    Location location  = new Location("");//provider name is unnecessary
    private GoogleMap map;
    int flag=0;
    Marker current_marker;
    // use it to request location updates and get the latest location
    private FusedLocationProviderClient fusedLocClient;
    private static final int REQUEST_LOCATION = 1;//request code to identify specific permission request
    private static final String TAG = "MapsFragment";// for debugging
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_maps, container, false);
        //Initialize map fragment
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment supportMapFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        //Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NotNull GoogleMap googleMap) {

                map = googleMap;//initialise map
                setupLocClient();
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        map = googleMap;//initialise map


                        getCurrentLocation(latLng.latitude,latLng.longitude);
                    }

                });


                }


         });

        //Return view
        return view;

    }






    private final void setupLocClient() {

        fusedLocClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

    }
    // prompt the user to grant/deny access
    private final void requestLocPermissions() {
        ActivityCompat.requestPermissions(this.getActivity(), new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
    }
    private final void getCurrentLocation(double latitude,double longitude) {
        if (ActivityCompat.checkSelfPermission( this.getContext(), "android.permission.ACCESS_FINE_LOCATION") != 0) {
            this.requestLocPermissions();
        } else {

  fusedLocClient.getLastLocation().addOnCompleteListener((OnCompleteListener)(new OnCompleteListener() {

      public final void onComplete(@NotNull Task it) {


          //Location location = (Location) it.getResult();
          if(flag==0){
          location = (Location) it.getResult();
          flag=1;}
          else{
          location.setLatitude(latitude) ;
          location.setLongitude(longitude);}

          if (location != null) {
              LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
              if(current_marker!=null){
                  current_marker.remove();
              }
              current_marker= map.addMarker((new MarkerOptions()).position(latLng).title("You are currently here!"));
              CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0F);
              map.moveCamera(update);


          }             else
              {
                       Log.e("MapsActivity", "No location found");
                 }
        }
                }));




        }


    }
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == 0) {
                this.getCurrentLocation(0,0);
            } else {
                Log.e("MapsActivity", "Location permission has been denied");
            }
        }

    }

    Location getLastLocation(){
        return location ;
    }



}