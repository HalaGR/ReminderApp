package com.example.locationreminder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class home_page_Activity extends AppCompatActivity {

    Button my_add_reminder_btn;
    Button my_sign_out_but;
    TextView verifyMsg;
    String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    //list of reminders
    RecyclerView myrecyclerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirebaseUser firebaseUser;
    FirestoreRecyclerAdapter<firebasemodel, ReminderViweHolder> reminderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //getSupportActionBar().setTitle("All Reminders:");
        //start for verification
        verifyMsg = findViewById(R.id.verifyMsg);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        FirebaseUser user = fAuth.getCurrentUser();

        //************************************

        //************************************
        if (!user.isEmailVerified()) {
            verifyMsg.setVisibility(View.VISIBLE);
            verifyMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void unused) {
                            Toast.makeText(view.getContext(), "Verification Email Has been Sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "onFailure: Email not sent" + e.getMessage());
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
                startActivity(new Intent(home_page_Activity.this, add_reminder.class));
                finish();
            }
        });

        //display reminders
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        Query query = fStore.collection("reminders").document(firebaseUser.getUid()).collection("myreminders").orderBy("title", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<firebasemodel> all_user_reminders = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query, firebasemodel.class).build();
        reminderAdapter = new FirestoreRecyclerAdapter<firebasemodel, ReminderViweHolder>(all_user_reminders) {
            @Override
            protected void onBindViewHolder(@NonNull ReminderViweHolder reminderViweHolder, int i, @NonNull firebasemodel firebasemodel) {
                reminderViweHolder.reminderTitle.setText(firebasemodel.getTitle());
                reminderViweHolder.reminderDescription.setText(firebasemodel.getDescription());
                reminderViweHolder.reminderDate.setText(firebasemodel.getDate());

            }

            @NonNull
            @Override
            public ReminderViweHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
                return new ReminderViweHolder(view);
            }
        };

        myrecyclerview = findViewById(R.id.recycleview);
        myrecyclerview.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        myrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        myrecyclerview.setAdapter(reminderAdapter);
    }

    public class ReminderViweHolder extends RecyclerView.ViewHolder {
        private TextView reminderTitle;
        private TextView reminderDescription;
        private TextView reminderDate;
        LinearLayout myreminders;

        public ReminderViweHolder(@NonNull View itemView) {
            super(itemView);
            reminderTitle = itemView.findViewById(R.id.notetitle);
            reminderDescription = itemView.findViewById(R.id.noteDescription);
            reminderDate = itemView.findViewById(R.id.noteDate);
            myreminders = itemView.findViewById(R.id.note);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        reminderAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (reminderAdapter != null){
            reminderAdapter.startListening();
        }

    }
}
