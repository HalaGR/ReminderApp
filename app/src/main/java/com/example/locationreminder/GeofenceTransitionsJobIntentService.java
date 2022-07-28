package com.example.locationreminder;




import android.app.Application;
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



public final class GeofenceTransitionsJobIntentService extends JobIntentService {
    private static final String LOG_TAG = "GeoTrIntentService";
    private static final int JOB_ID = 573;
    public static final GeofenceTransitionsJobIntentService.Companion Companion = new GeofenceTransitionsJobIntentService.Companion();

    protected void onHandleWork( Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.INSTANCE.getErrorString((Context)this, geofencingEvent.getErrorCode());
            Log.e("GeoTrIntentService", errorMessage);
        } else {
            this.handleEvent(geofencingEvent);
        }
    }

    private final void handleEvent(GeofencingEvent event) {
        if (event.getGeofenceTransition() == 1) {
            Reminder reminder = this.getFirstReminder(event.getTriggeringGeofences());
            String message = reminder != null ? reminder.getMessage() : null;
            LatLng latLng = reminder != null ? reminder.getLatLng() : null;
            if (message != null && latLng != null){// && reminder.getCurrentUser().equals(currentUser())) {
                Utils.sendNotification((Context)this, message, latLng,reminder.getTitle(),reminder.getDescription());
            }
        }

    }
    private final String currentUser(){
          FirebaseAuth fAuth= FirebaseAuth.getInstance();
          String userId = fAuth.getCurrentUser().getUid();
          return userId;
    }

    private final Reminder getFirstReminder(List triggeringGeofences) {
        Geofence firstGeofence = (Geofence)triggeringGeofences.get(0);
        // return ((ReminderApp)this.getApplication()).getRepository().get(firstGeofence.getRequestId());
        ReminderApp reminderApp=new ReminderApp(this.getApplication());
        return reminderApp.getRepository().get(firstGeofence.getRequestId());
    }

    public static final class Companion {
        public final void enqueueWork( Context context,  Intent intent) {
            JobIntentService.enqueueWork(context, GeofenceTransitionsJobIntentService.class, 573, intent);
        }
        public Companion() {}
    }

}

