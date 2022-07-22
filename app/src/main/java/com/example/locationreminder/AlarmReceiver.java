package com.example.locationreminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class    AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver(){

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("notification", "inside alarm receiver1");

        Intent i = new Intent(context,home_page_Activity.class);// change this to view activity later
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,0);

        //Snooze button
        Intent snoozeIntent = new Intent(context, AlarmReceiver.class);
        // snoozeIntent.setAction("Snooze");
        snoozeIntent.putExtra("Snooze", 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(context, 0, snoozeIntent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"foxandroid")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Foxandroid Alarm Manager")
                .setContentText("Subscribe for android related content")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.snooze_icon, "Snooze",
                snoozePendingIntent);
        Log.d("notification", "inside alarm receiver2");

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());
        Log.d("notification", "inside alarm receiver3");
        


    }
}
