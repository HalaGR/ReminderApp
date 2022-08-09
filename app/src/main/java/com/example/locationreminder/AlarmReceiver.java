package com.example.locationreminder;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class    AlarmReceiver extends BroadcastReceiver {
    /* send notification with the reminders details on the time set for it to pop up.
    (title and description will show as notification )
     */

    public AlarmReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {


        Intent i = new Intent(context,home_page_Activity.class);// change this to view activity later
        Bundle extras = intent.getExtras();
        String  title="";
        String  description="";
        int REQUEST_CODE=0;
        if (extras != null) {
             title = extras.getString("title");
             description = extras.getString("description");
            REQUEST_CODE = extras.getInt("REQUEST_CODE");
            Log.d("notification", description + "on receive");
            intent.removeExtra("title");
            intent.removeExtra("description");

        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,REQUEST_CODE,intent, PendingIntent.FLAG_CANCEL_CURRENT);

        //Snooze button
        Intent snoozeIntent = new Intent(context, AlarmReceiver.class);
        // snoozeIntent.setAction("Snooze");
        snoozeIntent.putExtra("Snooze", 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(context, REQUEST_CODE, snoozeIntent, PendingIntent.FLAG_ONE_SHOT);

        Log.d("notification", description + "on receive 2");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"foxandroid")
                .setSmallIcon(R.drawable.reminder_logo)
                .setContentTitle(title)
                .setContentText(description)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.snooze_icon, "Snooze",
                snoozePendingIntent);
        //Log.d("notification", "inside alarm receiver2");

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());
       // Log.d("notification", "inside alarm receiver3");
       // removeReminderFromFirebase(0);


    }
    public  final void removeReminderFromFirebase(int ID)
    {  FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        fStore.collection("reminders").whereEqualTo("ID",ID).get().getResult().getDocuments().get(0).getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void unused) {
                //new ServicesForLocation().removeReminder(locationDetails); <------ edit to be for time

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("tag", "Error deleting document");
            }
        });
    }
}
