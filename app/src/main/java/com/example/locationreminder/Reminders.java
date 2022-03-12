package com.example.locationreminder;
import java.util.concurrent.atomic.AtomicInteger;
import android.location.Location;

public class Reminders {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    private int ID;
    String title;
    String description;
    String Date;
    String time;
    Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public static AtomicInteger getIdGenerator() {
        return ID_GENERATOR;
    }

    public Reminders() {
        this.ID = ID_GENERATOR.getAndIncrement();
    }
}
