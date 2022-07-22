package com.example.locationreminder;


import android.app.Application;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
public abstract class BaseActivity extends AppCompatActivity {
    public final ReminderRepository getRepository() {
        /*Application app=((BaseActivity)this).getApplication();
        ReminderApp reminderApp=(ReminderApp)app;
         return reminderApp.getRepository();*/
        Application app=((BaseActivity)this).getApplication();
        ReminderApp reminderApp=new ReminderApp(app);
        return reminderApp.getRepository();

    }
}


