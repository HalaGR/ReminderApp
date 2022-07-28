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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.core.app.NotificationCompat;
//import com.android.raywenderlich.remindmethere.MainActivity.Companion;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
public final class Utils {
    private static final String NOTIFICATION_CHANNEL_ID = "android.support.v4.channel";

    public static final void requestFocusWithKeyboard(EditText text ) {
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


    public static final void hideKeyboard(Context context,View view) {
        InputMethodManager keyboard = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }


    public static final BitmapDescriptor vectorToBitmap( Resources resources, int id) {
        Drawable vectorDrawable =getDrawable(resources, id, (Theme)null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static final void showReminderInMap( Context context, GoogleMap map, Reminder reminder) {
        if (reminder.getLatLng() != null) {
            LatLng latLng = reminder.getLatLng();
            BitmapDescriptor vectorToBitmap = vectorToBitmap(context.getResources(),  R.drawable.ic_twotone_location_on_48px);
            Marker marker = map.addMarker((new MarkerOptions()).position(latLng).icon(vectorToBitmap));
            marker.setTag(reminder.getId());
            if (reminder.getRadius() != null) {
                Double var9 = reminder.getRadius();
                double radius = var9;
                map.addCircle((new CircleOptions()).center(reminder.getLatLng()).radius(radius).strokeColor(getColor(context, R.color.colorAccent)).fillColor(getColor(context, R.color.colorReminderFill)));
            }
        }

    }

    public static final void sendNotification( Context context,  String message,  LatLng latLng,String title,String description) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            String name = context.getString(R.string.app_name);
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, (CharSequence)name, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = ControlActivity.Companion.newIntent(context.getApplicationContext(), latLng);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context).addParentStack(ControlActivity.class).addNextIntent(intent);
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(getUniqueId(), PendingIntent.FLAG_UPDATE_CURRENT);
        //Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).setSmallIcon(R.drawable.reminder_logo).setContentTitle((CharSequence)message).setContentIntent(notificationPendingIntent).setAutoCancel(true).build();
        Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).setSmallIcon(R.drawable.reminder_logo).setContentTitle( title).setContentText("We are in "+message+" "+description).setAutoCancel(true).build();
        notificationManager.notify(getUniqueId(), notification);

    }

    private static final int getUniqueId() {
        return (int)(System.currentTimeMillis() % (long)10000);
    }
}

