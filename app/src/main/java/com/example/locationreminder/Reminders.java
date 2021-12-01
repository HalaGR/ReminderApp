package com.example.locationreminder;
import java.util.concurrent.atomic.AtomicInteger;

public class Reminders {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    private int ID;
    String Title;
    String description;
    String Date;
    String time;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
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
