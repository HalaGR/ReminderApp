package com.example.locationreminder;


import android.app.Application;
import android.content.Context;

public final class ReminderApp extends Application {
    private ReminderRepository repository;
    Application application;
    public ReminderApp(Application application){
        super();
        this.application=application;
        super.onCreate();
        this.repository = new ReminderRepository(application);

    }
    /* public void onCreate() {
         super.onCreate();
         this.repository = new ReminderRepository((ReminderApp)this);

     }*/
    public final ReminderRepository getRepository() {
        return this.repository;
    }
}
