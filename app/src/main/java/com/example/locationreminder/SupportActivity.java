package com.example.locationreminder;
import android.app.Application;
import androidx.appcompat.app.AppCompatActivity;
public abstract class SupportActivity extends AppCompatActivity {
    public final LocationStoreHouse getStoreHouse() {
        Application app=((SupportActivity)this).getApplication();
        LocationApplication locationApplication =new LocationApplication(app);
        return locationApplication.getStoreHouse();

    }
}


