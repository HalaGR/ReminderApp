package com.example.locationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class home_page_Activity extends AppCompatActivity {

    Button my_add_reminder_btn;
    Button my_sign_out_but;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportActionBar().setTitle("All Reminders:");
        my_sign_out_but = findViewById(R.id.sign_out_but);
        my_sign_out_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    startActivity(new Intent(MainActivity.this, login.class)); // go to log in page
            }
        });
        my_add_reminder_btn = findViewById(R.id.add_reminder_but);
        my_add_reminder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //go to add reminder page on click
                startActivity(new Intent(home_page_Activity.this, Reminders.class));
            }
        });

    }


}