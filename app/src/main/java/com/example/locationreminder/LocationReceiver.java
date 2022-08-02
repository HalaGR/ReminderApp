package com.example.locationreminder;


import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
public final class LocationReceiver extends BroadcastReceiver {
    public void onReceive( Context context, Intent intent) {
        PreparingForLocationNotification.Companion.enqueueWork(context, intent);
    }
}
