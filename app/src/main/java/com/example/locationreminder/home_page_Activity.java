package com.example.locationreminder;

import android.content.Intent;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import android.view.ContextMenu;

import android.view.MenuItem;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class home_page_Activity extends AppCompatActivity {


    TextView my_add_reminder_btn;
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
    public static final String TAG = "";


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
                LoginActivity loginActivity=new LoginActivity();
                startActivity(new Intent(home_page_Activity.this,loginActivity.getClass())); // go to log in page
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
        String[] months = new DateFormatSymbols().getShortMonths();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        Query query = fStore.collection("reminders").document(firebaseUser.getUid()).collection("myreminders").orderBy("title", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<firebasemodel> all_user_reminders = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query, firebasemodel.class).build();
        reminderAdapter = new FirestoreRecyclerAdapter<firebasemodel, ReminderViweHolder>(all_user_reminders) {
            @Override
            protected void onBindViewHolder(@NonNull ReminderViweHolder reminderViweHolder, int i, @NonNull firebasemodel firebasemodel) {
                reminderViweHolder.reminderTitle.setText(firebasemodel.getTitle());
                reminderViweHolder.reminderDescription.setText(firebasemodel.getDescription());
                if(firebasemodel.getTime()==null){
                    reminderViweHolder.mytime="";
                }else{
                reminderViweHolder.mytime=firebasemodel.getTime();}
                //reminderViweHolder.mylocation=firebasemodel.getLocation();
                if(firebasemodel.getLocation().size()==0){
                    reminderViweHolder.mylocation_list.add(0.0);
                    reminderViweHolder.mylocation_list.add(0.0);}
                else{
                    reminderViweHolder.mylocation_list=firebasemodel.getLocation();}
                if(firebasemodel.getWeather().size()==0){
                reminderViweHolder.myweather.add("");
                reminderViweHolder.myweather.add("");}
                else{
                reminderViweHolder.myweather=firebasemodel.getWeather();}
                if(firebasemodel.getDate()==null){
                    reminderViweHolder.mydate="";
                }else{
                reminderViweHolder.mydate=firebasemodel.getDate();}
                String[] list_date;
                if(firebasemodel.getDate()!=null) {
                    list_date = firebasemodel.getDate().split("/");

                    reminderViweHolder.myday.setText(list_date[0]);
                    reminderViweHolder.mymonth.setText(months[Integer.valueOf(list_date[1])-1]);
                    reminderViweHolder.myyear.setText(list_date[2]);}


                reminderViweHolder.Key=all_user_reminders.getSnapshots().getSnapshot(i).getId();







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
        private TextView myday;
        private TextView myyear;
        private TextView mymonth;
        private ImageView myoptions;
        private String mytime="";
        private String mydate="";
        private String Key;
        private Location mylocation=new Location("");
        private List<String> myweather= new ArrayList<String>();
        private List<Double> mylocation_list= new ArrayList<Double>();
        LinearLayout myreminders;


        public ReminderViweHolder(@NonNull View itemView) {
            super(itemView);
            reminderTitle = itemView.findViewById(R.id.notetitle);
            reminderDescription = itemView.findViewById(R.id.noteDescription);
            myday=itemView.findViewById(R.id.day);
            myyear=itemView.findViewById(R.id.year);
            mymonth=itemView.findViewById(R.id.month);
            myoptions=itemView.findViewById(R.id.options);
            registerForContextMenu(myoptions);


              myoptions.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      PopupMenu popupMenu = new PopupMenu(home_page_Activity.this, view);
                      popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
                      popupMenu.setOnMenuItemClickListener(item -> {
                          switch (item.getItemId()) {
                              case R.id.menuDelete:
                                  fStore.collection("reminders").document(firebaseUser.getUid()).collection("myreminders").document(Key).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                      @Override
                                      public void onSuccess(@NonNull Void unused) {
                                          Toast.makeText(view.getContext(), "Reminder successfully deleted!", Toast.LENGTH_SHORT).show();
                                      }
                                  }).addOnFailureListener(new OnFailureListener() {
                                      @Override
                                      public void onFailure(@NonNull Exception e) {
                                          Log.d("tag", "Error deleting document");
                                      }
                                  });
                                  break;
                              case R.id.menuEdit:
                                  Intent n= new Intent(home_page_Activity.this, add_reminder.class);
                                  //n.putExtra("from","home");
                                  n.putExtra("Key",Key);
                                  //n.putExtra("mytime",fStore.collection("reminders").document(firebaseUser.getUid()).collection("myreminders").document(Key).get().getResult().get("time").toString());
                                  n.putExtra("mytime",mytime);
                                  n.putExtra("mytitleinput",reminderTitle.getText());
                                  mylocation.setLatitude(mylocation_list.get(0));
                                  mylocation.setLongitude(mylocation_list.get(1));
                                  n.putExtra("location",mylocation);
                                  n.putExtra("mydescriptioninput",reminderDescription.getText());
                                  n.putExtra("mydate",mydate);
                                  n.putExtra("mycity",myweather.get(0));
                                  n.putExtra("mycondition",myweather.get(1));


                                  startActivity(n);


                                  break;
                          }
                          return false;
                      });
                      popupMenu.show();
                  }

              });

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
