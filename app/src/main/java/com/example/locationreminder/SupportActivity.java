package com.example.locationreminder;
import android.app.Application;
import androidx.appcompat.app.AppCompatActivity;
public abstract class SupportActivity extends AppCompatActivity {
    /*
    * we made this AppCompatActivity class to be able to do inheritance for a number of classes to this base class
    *  we wanna use a single LocationStoreHouse object
       from a different classes.
       * */
    public final LocationStoreHouse getStoreHouse() {
        Application app=((SupportActivity)this).getApplication();
        LocationApplication locationApplication =new LocationApplication(app);
        return locationApplication.getStoreHouse();

    }
}


