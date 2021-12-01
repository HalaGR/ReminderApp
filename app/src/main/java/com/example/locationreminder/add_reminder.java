package com.example.locationreminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class add_reminder extends AppCompatActivity {

    EditText mytitleinput, mydescriptioninput;
    FloatingActionButton mysavebtn;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebasefirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        mytitleinput = findViewById(R.id.titleinput);
        mydescriptioninput = findViewById(R.id.descriptioninput);
        mysavebtn = findViewById(R.id.savebtn);

        Toolbar toolbar = findViewById(R.id.toolbarofaddreminder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get current user database
        firebaseAuth = FirebaseAuth.getInstance();
        firebasefirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mysavebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String title = mytitleinput.getText().toString();
                String description = mydescriptioninput.getText().toString();
                if (title.isEmpty() || description.isEmpty()){
                    //are all files filed
                    Toast.makeText(getApplicationContext(), "Pleas fill Bothe files", Toast.LENGTH_SHORT).show();
                }else{
                    // add reminder to database
                    DocumentReference documentReference = firebasefirestore.collection("reminders").document(firebaseUser.getUid()).collection("myreminders").document();
                    Map<String, Object> reminder = new HashMap<>();
                    reminder.put("title", title);
                    reminder.put("description", description);
                    documentReference.set(reminder).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // reminder saved successfully
                            // send feedback and go back to home page
                            Toast.makeText(getApplicationContext(), "Reminder saved successfully, we'll remind you!" , Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(add_reminder.this, home_page_Activity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Something went wrong, Pleas try again!" , Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            // add a back arrow in toolbar
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
