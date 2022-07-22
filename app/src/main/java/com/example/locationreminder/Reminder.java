package com.example.locationreminder;



import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import java.util.UUID;


public final class Reminder {
    private final String id;
    private LatLng latLng;
    private Double radius;
    private String message;


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


    public Reminder( LatLng latLng,  Double radius, String message) {
        super();
        this.id = UUID.randomUUID().toString();
        this.latLng = latLng;
        this.radius = radius;
        this.message = message;
    }
    public Reminder( String id, LatLng latLng,  Double radius, String message) {
        super();
        this.id =id;
        this.latLng = latLng;
        this.radius = radius;
        this.message = message;
    }




}

