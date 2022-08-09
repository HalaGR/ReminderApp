package com.example.locationreminder;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class ServicesForLocation extends SupportActivity{
    @RequiresApi(api = Build.VERSION_CODES.N)
    public  void removeReminder(LocationDetails locationDetails) {
        this.getStoreHouse().remove(locationDetails,(Callable) (new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                return null;
            }
        }),  (Function) (new Function<String,Void>() {
            @Override
            public Void apply(String it) {
                return null;
            }


        }));

    }
}
