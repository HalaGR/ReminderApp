package com.example.locationreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public final class GeofenceBroadcastReceiver extends BroadcastReceiver {
    public void onReceive( Context context, Intent intent) {
        GeofenceTransitionsJobIntentService.Companion.enqueueWork(context, intent);
    }
}
