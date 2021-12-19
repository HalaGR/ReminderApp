package com.example.locationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    Button my_add_reminder_btn;
    Button my_sign_out_but;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //getSupportActionBar().setTitle("All Reminders:");
        my_sign_out_but = findViewById(R.id.sign_out_but);
        my_sign_out_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   FirebaseAuth.getInstance().signOut();
                   startActivity(new Intent(MainActivity.this, LoginActivity.class)); // go to log in page
                   finish();
            }
        });
        my_add_reminder_btn = findViewById(R.id.add_reminder_but);
        my_add_reminder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //go to add reminder page on click
                startActivity(new Intent(MainActivity.this, add_reminder.class));
            }
        });

    }


}