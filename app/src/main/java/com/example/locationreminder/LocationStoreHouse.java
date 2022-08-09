package com.example.locationreminder;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
//import android.support.v4.content.ContextCompat;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Geofence.Builder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

public final class LocationStoreHouse {
    /*
     In this class  we save all location reminders for every user in SharedPreferences for restore it
      also we did add,remove,search,get last, get all functions for locations
    * */
    private final SharedPreferences preferences;
    private final GeofencingClient geofencingClient;
    private final Gson gson;
    private final Context context;
    private static final String PREFS_NAME = "LocationStoreHouse";
    public static final LocationStoreHouse.Companion Companion = new LocationStoreHouse.Companion();


    private final FirebaseAuth fAuth= FirebaseAuth.getInstance();
    private final String userId = fAuth.getCurrentUser().getUid();
    private final String REMINDERS = userId;
//******************************added to translate add to add_reminder -start**********************
    private LocationDetails locationDetails;
    public  void setReminder( LocationDetails locationDetails){
        this.locationDetails = locationDetails;

    }
    public LocationDetails getReminder(){
        return this.locationDetails;

    }
//******************************added to translate add to add_reminder -end************************
    private final PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(LocationStoreHouse.this.context, LocationReceiver.class);
        return PendingIntent.getBroadcast(LocationStoreHouse.this.context, 0, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
    }
    public final void saveLocation(final LocationDetails locationDetails){
        List<LocationDetails> reminders_= LocationStoreHouse.this.getEachOfLocations();
        reminders_.add(locationDetails);
        LocationStoreHouse.this.saveEachOfLocations(reminders_);
    }
    public final void add(final LocationDetails locationDetails, final Callable success, final Function failure) {
        locationDetails.setCurrentUser(userId);
        Geofence geofence = this.locationToBuild(locationDetails);
        if (geofence != null && ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.geofencingClient.addGeofences(this.LocationRequestToBuild(geofence), this.getGeofencePendingIntent()).addOnSuccessListener((OnSuccessListener)(new OnSuccessListener() {
                public void onSuccess(Object var1) {
                    //List<LocationDetails> reminders_=LocationStoreHouse.this.getEachOfLocations();
                   // reminders_.add(locationDetails);deleted for my app
                   // LocationStoreHouse.this.saveEachOfLocations(reminders_);deleted for my app
                    try {
                        success.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            })).addOnFailureListener((OnFailureListener)(new OnFailureListener() {

                public final void onFailure(Exception it) {
                    failure.apply(ErrorMessagesForLocation.INSTANCE.errorService(LocationStoreHouse.this.context, it));
                }
            }));
        }

    }
    public final void remove(final LocationDetails locationDetails, final Callable success, final Function failure) {
        this.geofencingClient.removeGeofences(listOf(locationDetails.getId())).addOnSuccessListener((OnSuccessListener)(new OnSuccessListener() {
            public void onSuccess(Object var1) {
                List<LocationDetails> reminders_= LocationStoreHouse.this.getEachOfLocations();
                int index=0;
                for (LocationDetails it : reminders_) {
                    //LocationDetails it = (LocationDetails)this.getEachOfLocations().iterator().next();
                    if (!it.getId().equals(locationDetails.getId())) {
                        index+=1;
                    }
                    else{break;}
                }
                reminders_.remove(index);
                LocationStoreHouse.this.saveEachOfLocations(reminders_);
                try {
                    success.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })).addOnFailureListener((OnFailureListener)(new OnFailureListener() {

            public final void onFailure(Exception it) {
                failure.apply(ErrorMessagesForLocation.INSTANCE.errorService(LocationStoreHouse.this.context, it));

            }
        }));
    }


    private final GeofencingRequest LocationRequestToBuild(Geofence geofence) {
        return (new com.google.android.gms.location.GeofencingRequest.Builder()).setInitialTrigger(0).addGeofences(listOf(geofence)).build();
    }

    private final Geofence locationToBuild(LocationDetails locationDetails) {
        Double latitude = locationDetails.getLatLng() != null ? locationDetails.getLatLng().latitude : null;
        Double longitude = locationDetails.getLatLng() != null ? locationDetails.getLatLng().longitude : null;
        Double radius = locationDetails.getRadius();
        return latitude != null && longitude != null && radius != null ? (new Builder()).setRequestId(locationDetails.getId()).setCircularRegion(latitude, longitude, radius.floatValue()).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER).setExpirationDuration(Geofence.NEVER_EXPIRE).build() : null;
    }




    public final LocationDetails getFinal() {
        return (LocationDetails)this.getEachOfLocations().get(this.getEachOfLocations().size() - 1);

    }

    public final LocationDetails search(String requestId) {
        List<LocationDetails> reminders_=this.getEachOfLocations();
        for (LocationDetails it : reminders_) {
            //LocationDetails it = (LocationDetails)this.getEachOfLocations().iterator().next();
            if (it.getId().equals(requestId)) {
                return it;
            }
            //return it;
        }

        return null;
    }

    private final void saveEachOfLocations(List list) {
        this.preferences.edit().putString(REMINDERS, this.gson.toJson(list)).apply();
    }

    public final List<LocationDetails> getEachOfLocations() {
        if (this.preferences.contains(REMINDERS)) {
            String remindersString = this.preferences.getString(REMINDERS, (String)null);
            LocationDetails[] arrayOfLocationDetails = (LocationDetails[])this.gson.fromJson(remindersString, LocationDetails[].class);
            if (arrayOfLocationDetails != null) {
                int x= arrayOfLocationDetails.length;
                ArrayList arr=new ArrayList(listOf(arrayOfLocationDetails));
                //List arr= listOf(arrayOfLocationDetails);
                return arr;

            }
        }
        List l=new LinkedList();
        return l;
    }





    public LocationStoreHouse(Context context) {
        super();
        this.context = context;
        this.preferences = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
        this.geofencingClient = LocationServices.getGeofencingClient(this.context);
        // this.geofencePendingIntent$delegate = LazyKt.lazy((Function0)(new Function0() {

    }

    public static final class Companion {
        private Companion() {
        }
        // public Companion(DefaultConstructorMarker $constructor_marker) {
        //   this();
    }
}



