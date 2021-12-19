package com.example.locationreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class home_page_Activity extends AppCompatActivity {

    Button my_add_reminder_btn;
    Button my_sign_out_but;
    TextView verifyMsg;
    String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //getSupportActionBar().setTitle("All Reminders:");
        //start for verification
        verifyMsg=findViewById(R.id.verifyMsg);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        userId=fAuth.getCurrentUser().getUid();
        FirebaseUser user=fAuth.getCurrentUser();
        if(!user.isEmailVerified()){
            verifyMsg.setVisibility(View.VISIBLE);
            verifyMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void unused) {
                            Toast.makeText(view.getContext(),"Verification Email Has been Sent",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "onFailure: Email not sent"+e.getMessage());
                        }
                    });


                }
            });
        }
        //end for verification
        my_sign_out_but = findViewById(R.id.sign_out_but);
        my_sign_out_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(home_page_Activity.this, LoginActivity.class)); // go to log in page
                finish();
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