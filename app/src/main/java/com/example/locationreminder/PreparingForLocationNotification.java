package com.example.locationreminder;




import android.content.Context;
import android.content.Intent;
//import android.support.v4.app.JobIntentService;
import androidx.core.app.JobIntentService;
import android.util.Log;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;



public final class PreparingForLocationNotification extends JobIntentService {
    public static final PreparingForLocationNotification.Companion Companion = new PreparingForLocationNotification.Companion();

    protected void onHandleWork( Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String error1 = ErrorMessagesForLocation.INSTANCE.errorService((Context)this, geofencingEvent.getErrorCode());
            Log.e("GeoTrIntentService", error1);
        } else {
            this.handleEvent(geofencingEvent);
        }
    }


    private final String currentUser(){
          FirebaseAuth fAuth= FirebaseAuth.getInstance();
          String userId = fAuth.getCurrentUser().getUid();
          return userId;
    }

    private final LocationDetails getTopLocation(List triggeringGeofences) {
        Geofence firstGeofence = (Geofence)triggeringGeofences.get(0);
        // return ((LocationApplication)this.getApplication()).getStoreHouse().search(firstGeofence.getRequestId());
        LocationApplication locationApplication =new LocationApplication(this.getApplication());
        return locationApplication.getStoreHouse().search(firstGeofence.getRequestId());
    }
    private final void handleEvent(GeofencingEvent event) {
        if (event.getGeofenceTransition() == 1) {
            LocationDetails locationDetails = this.getTopLocation(event.getTriggeringGeofences());
            String message = locationDetails != null ? locationDetails.getMessage() : null;
            LatLng latLng = locationDetails != null ? locationDetails.getLatLng() : null;
            if (message != null && latLng != null){// && locationDetails.getCurrentUser().equals(currentUser())) {
                Services.sendNotification((Context)this, message, latLng, locationDetails.getTitle(), locationDetails.getDescription());
            }
        }

    }

    public static final class Companion {
        public final void enqueueWork( Context context,  Intent intent) {
            JobIntentService.enqueueWork(context, PreparingForLocationNotification.class, 573, intent);
        }
        public Companion() {}
    }

}

