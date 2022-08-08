package com.example.locationreminder;


import android.content.Context;
import android.content.res.Resources;
import com.google.android.gms.common.api.ApiException;

public final class ErrorMessagesForLocation {
    /* class for error messages related to location
     */

    public static final ErrorMessagesForLocation INSTANCE;
    public final String errorService(Context context, int errorCode) {
        Resources resources = context.getResources();
        switch(errorCode) {
            case 1000:
                return "Geofence service is not available now. Go to Settings>Location>Mode and choose High accuracy.";
            case 1001:
                return "Your app has registered too many geofences.";
            case 1002:
                return "You have provided too many PendingIntents to the addGeofences() call.";
            default:
                return " Unknown error: the Geofence service is not available now.";

        }


    }
    public final String errorService(Context context, Exception e) {
        if (e instanceof ApiException) {
            return this.errorService(context, ((ApiException)e).getStatusCode());
        } else {
            return "Unknown error: the Location service is not available now.";

        }

    }

    private ErrorMessagesForLocation() {
    }

    static {
        INSTANCE = new ErrorMessagesForLocation();
    }
}


