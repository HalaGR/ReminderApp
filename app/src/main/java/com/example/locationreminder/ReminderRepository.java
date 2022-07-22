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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

public final class ReminderRepository {
    private final SharedPreferences preferences;
    private final Gson gson;
    private final GeofencingClient geofencingClient;
    private final Context context;
    private static final String PREFS_NAME = "ReminderRepository";
    private static final String REMINDERS = "REMINDERS";
    public static final ReminderRepository.Companion Companion = new ReminderRepository.Companion();
//******************************added to translate add to add_reminder -start**********************
    private  Reminder reminder;
    public  void setReminder( Reminder reminder){
        this.reminder=reminder;

    }
    public Reminder getReminder(){
        return this.reminder;

    }
//******************************added to translate add to add_reminder -end************************
    private final PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(ReminderRepository.this.context, GeofenceBroadcastReceiver.class);
        return PendingIntent.getBroadcast(ReminderRepository.this.context, 0, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
    }
    public final void saveReminder(final Reminder reminder){
        List<Reminder> reminders_=ReminderRepository.this.getAll();
        reminders_.add(reminder);
        ReminderRepository.this.saveAll(reminders_);
    }
    public final void add(final Reminder reminder, final Callable success, final Function failure) {
        Geofence geofence = this.buildGeofence(reminder);
        if (geofence != null && ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.geofencingClient.addGeofences(this.buildGeofencingRequest(geofence), this.getGeofencePendingIntent()).addOnSuccessListener((OnSuccessListener)(new OnSuccessListener() {
                public void onSuccess(Object var1) {
                    //List<Reminder> reminders_=ReminderRepository.this.getAll();
                   // reminders_.add(reminder);deleted for my app
                   // ReminderRepository.this.saveAll(reminders_);deleted for my app
                    try {
                        success.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            })).addOnFailureListener((OnFailureListener)(new OnFailureListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                public final void onFailure(Exception it) {
                    failure.apply(GeofenceErrorMessages.INSTANCE.getErrorString(ReminderRepository.this.context, it));
                }
            }));
        }

    }

    private final Geofence buildGeofence(Reminder reminder) {
        Double latitude = reminder.getLatLng() != null ? reminder.getLatLng().latitude : null;
        Double longitude = reminder.getLatLng() != null ? reminder.getLatLng().longitude : null;
        Double radius = reminder.getRadius();
        return latitude != null && longitude != null && radius != null ? (new Builder()).setRequestId(reminder.getId()).setCircularRegion(latitude, longitude, radius.floatValue()).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER).setExpirationDuration(Geofence.NEVER_EXPIRE).build() : null;
    }

    private final GeofencingRequest buildGeofencingRequest(Geofence geofence) {
        return (new com.google.android.gms.location.GeofencingRequest.Builder()).setInitialTrigger(0).addGeofences(listOf(geofence)).build();
    }

    public final void remove(final Reminder reminder, final Callable success, final Function failure) {
        this.geofencingClient.removeGeofences(listOf(reminder.getId())).addOnSuccessListener((OnSuccessListener)(new OnSuccessListener() {
            public void onSuccess(Object var1) {
                List<Reminder> reminders_=ReminderRepository.this.getAll();
                int index=0;
                for (Reminder it : reminders_) {
                    //Reminder it = (Reminder)this.getAll().iterator().next();
                    if (!it.getId().equals(reminder.getId())) {
                        index+=1;
                    }
                    else{break;}
                }
                reminders_.remove(index);
                ReminderRepository.this.saveAll(reminders_);
                try {
                    success.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })).addOnFailureListener((OnFailureListener)(new OnFailureListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public final void onFailure(Exception it) {
                failure.apply(GeofenceErrorMessages.INSTANCE.getErrorString(ReminderRepository.this.context, it));

            }
        }));
    }

    private final void saveAll(List list) {
        this.preferences.edit().putString(REMINDERS, this.gson.toJson(list)).apply();
    }

    public final List<Reminder> getAll() {
        if (this.preferences.contains(REMINDERS)) {
            String remindersString = this.preferences.getString(REMINDERS, (String)null);
            Reminder[] arrayOfReminders = (Reminder[])this.gson.fromJson(remindersString, Reminder[].class);
            if (arrayOfReminders != null) {
                int x=arrayOfReminders.length;
                ArrayList arr=new ArrayList(listOf(arrayOfReminders));
                //List arr= listOf(arrayOfReminders);
                return arr;

            }
        }
        List l=new LinkedList();
        return l;
    }


    public final Reminder get( String requestId) {
        List<Reminder> reminders_=this.getAll();
        for (Reminder it : reminders_) {
            //Reminder it = (Reminder)this.getAll().iterator().next();
            if (it.getId().equals(requestId)) {
                return it;
            }
            //return it;
        }

        return null;
    }


    public final Reminder getLast() {
        return (Reminder)this.getAll().get(this.getAll().size() - 1);

    }


    public ReminderRepository( Context context) {
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



