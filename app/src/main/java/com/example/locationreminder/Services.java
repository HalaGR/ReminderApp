package com.example.locationreminder;
import static androidx.core.content.ContextCompat.getColor;
import static androidx.core.content.res.ResourcesCompat.getDrawable;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
//import android.support.annotation.DrawableRes;
//import android.support.v4.app.TaskStackBuilder;
//import android.support.v4.app.NotificationCompat.Builder;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.res.ResourcesCompat;
import android.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
//import com.android.raywenderlich.remindmethere.MainActivity.Companion;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Callable;
import java.util.function.Function;

public final class Services{

    /* in this class we can find functions that will help for saving location
      like view location, send notification,
     calculate numbers etc*/
    private static final String CHANNEL_NOTIFICATION = "android.support.v4.channel";

    private static final int specialNum() {
        return (int)(System.currentTimeMillis() % (long)10000);
    }
    public static final void viewLocation(Context context, GoogleMap map, LocationDetails locationDetails) {
        if (locationDetails.getLatLng() != null) {
            LatLng latLng = locationDetails.getLatLng();
            BitmapDescriptor vectorToBitmap = BitmapDescriptorByUsingVector(context.getResources(),  R.drawable.ic_baseline_location_on_24);
            Marker marker = map.addMarker((new MarkerOptions()).position(latLng).icon(vectorToBitmap));
            marker.setTag(locationDetails.getId());
            if (locationDetails.getRadius() != null) {
                Double var9 = locationDetails.getRadius();
                double radius = var9;
                map.addCircle((new CircleOptions()).center(locationDetails.getLatLng()).radius(radius).strokeColor(getColor(context, R.color.colorAccent)).fillColor(getColor(context, R.color.colorReminderFill)));
            }
        }

    }
    public static final void sendNotification( Context context,  String message,  LatLng latLng,String title,String description) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager.getNotificationChannel(CHANNEL_NOTIFICATION) == null) {
            String name = context.getString(R.string.app_name);
            NotificationChannel channel = new NotificationChannel(CHANNEL_NOTIFICATION, (CharSequence)name, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }



        Intent intent = ControlActivity.Companion.newIntent(context.getApplicationContext(), latLng);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context).addParentStack(ControlActivity.class).addNextIntent(intent);
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(specialNum(), PendingIntent.FLAG_UPDATE_CURRENT);
        //Notification notification = new NotificationCompat.Builder(context, CHANNEL_NOTIFICATION).setSmallIcon(R.drawable.reminder_logo).setContentTitle((CharSequence)message).setContentIntent(notificationPendingIntent).setAutoCancel(true).build();
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_NOTIFICATION).setSmallIcon(R.drawable.reminder_logo).setContentTitle(title).setContentText(description).setAutoCancel(true).build();
        notificationManager.notify(specialNum(), notification);

    }

    public static final BitmapDescriptor BitmapDescriptorByUsingVector(Resources resources, int id) {
        Drawable vectorDrawable =getDrawable(resources, id, (Theme)null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    public static final void coverConsole(Context context, View view) {
        InputMethodManager keyboard = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }





    public static final void FocusingWithConsole(EditText text ) {
        final InputMethodManager imm =(InputMethodManager) text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!text.hasFocus()) {
            text.requestFocus();
        }

        text.post((Runnable)(new Runnable() {
            public final void run() {
                imm.showSoftInput((View)text, InputMethodManager.SHOW_FORCED);
            }
        }));
    }
}

