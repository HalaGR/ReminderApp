package com.example.locationreminder;



import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import java.util.UUID;


public final class LocationDetails {
    /* in this class we save all data about location reminder in it to reuse this information for
     saving location and showing notification for user*/
    private final String id;
    private LatLng latLng;
    private Double radius;
    private String message;
    private String currentUser;
    private String title;
    private String description;
    public final String getTitle() {
        return this.title;
    }

    public final String getDescription() {
        return this.description;
    }

    public final void setTitle( String title) {
        this.title = title;
    }

    public final void setDescription( String description) {
        this.description = description;
    }

    public final String getCurrentUser() {
        return this.currentUser;
    }
    public final void setCurrentUser( String currentUser) {
        this.currentUser = currentUser;
    }


    public final String getId() {
        return this.id;
    }

    public final LatLng getLatLng() {
        return this.latLng;
    }

    public final void setLatLng( LatLng var1) {
        this.latLng = var1;
    }


    public final Double getRadius() {
        return this.radius;
    }

    public final void setRadius( Double var1) {
        this.radius = var1;
    }


    public final String getMessage() {
        return this.message;
    }

    public final void setMessage( String var1) {
        this.message = var1;
    }


    public LocationDetails(LatLng latLng, Double radius, String message) {
        super();
        this.id = UUID.randomUUID().toString();
        this.latLng = latLng;
        this.radius = radius;
        this.message = message;
    }
    public LocationDetails(String id, LatLng latLng, Double radius, String message) {
        super();
        this.id =id;
        this.latLng = latLng;
        this.radius = radius;
        this.message = message;
    }




}

