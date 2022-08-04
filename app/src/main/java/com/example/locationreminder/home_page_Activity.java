package com.example.locationreminder;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;


public class home_page_Activity extends SupportActivity {
/* in this activity class we bring the data that user saved before (collection of reminders) from firebase and view it
in list ,also he can click on add reminder button to add new one, and in case the user didn't Verified her email before
then a small note will appear to click on it to do verification.
user can do sign out.
this class is related to activity_home_page.xml

 */

    TextView my_add_reminder_btn;
    Button my_sign_out_but;
    TextView verifyMsg;
    String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ImageView myfirstnote;

    //list of reminders
    RecyclerView myrecyclerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirebaseUser firebaseUser;
    FirestoreRecyclerAdapter<firebasemodel, ReminderViweHolder> reminderAdapter;
    public static final String TAG = "";
    List<firebasemodel> namedGeofences= new ArrayList<>();

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
        //this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //************************************

        //**************************************************start-for location notification-are we there yet************************************************
        // region GeofenceControllerListener

        /* GeofenceController.GeofenceControllerListener geofenceControllerListener = new GeofenceController.GeofenceControllerListener() {
            @Override
            public void onGeofencesUpdated() {

                 refresh();
            }

            @Override
            public void onError() {

                showErrorToast();
            }
        };*/
//        GeofenceController.getInstance(). setNamedGeofences(namedGeofences,geofenceControllerListener);
        //  private void showErrorToast() {
        //    Toast.makeText(getActivity(), getActivity().getString(R.string.Toast_Error), Toast.LENGTH_SHORT).show();
        // }

        // endregion
        //************************************
        //**************************************************end-for location notification-are we there yet************************************************
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
        myfirstnote=findViewById(R.id.firstnote);
        Query query = fStore.collection("reminders").document(firebaseUser.getUid()).collection("myreminders").orderBy("title", Query.Direction.ASCENDING);
        //if (fStore.collection("reminders").document(firebaseUser.getUid()).collection("myreminders").search().getResult().getDocuments().isEmpty()) myfirstnote.setVisibility(View.VISIBLE);
        FirestoreRecyclerOptions<firebasemodel> all_user_reminders = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query, firebasemodel.class).build();
       // if( all_user_reminders.getSnapshots().isEmpty())myfirstnote.setVisibility(View.VISIBLE);
        reminderAdapter = new FirestoreRecyclerAdapter<firebasemodel, ReminderViweHolder>(all_user_reminders) {
            @Override
            protected void onBindViewHolder(@NonNull ReminderViweHolder reminderViweHolder, int i, @NonNull firebasemodel firebasemodel) {
                namedGeofences.add(firebasemodel);
                //if(firebasemodel==null&& i==0)myfirstnote.setVisibility(View.VISIBLE);
                reminderViweHolder.reminderTitle.setText(firebasemodel.getTitle());
                reminderViweHolder.reminderDescription.setText(firebasemodel.getDescription());
                if(firebasemodel.getReminder()!=null){
                    reminderViweHolder.reminder=firebasemodel.getReminder();
                }
                if(firebasemodel.getTime()==null){
                    reminderViweHolder.mytime="";
                }else{
                reminderViweHolder.mytime=firebasemodel.getTime();}
                //reminderViweHolder.mylocation=firebasemodel.getLocation();


                if(firebasemodel.getDate()==null){
                    reminderViweHolder.mydate="";
                }else{
                reminderViweHolder.mydate=firebasemodel.getDate();}
                String[] list_date;
                if(firebasemodel.getDate()!=null && firebasemodel.getDate()!="") {
                    list_date = firebasemodel.getDate().split("/");

                    reminderViweHolder.myday.setText(list_date[0]);
                    reminderViweHolder.mymonth.setText(months[Integer.parseInt(list_date[1])-1]);
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
        private List<Object> reminder;
        private String mydate="";
        private String Key;
        private Location mylocation=new Location("");
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
                                          if (reminder!=null){
                                              if (reminder.size()!=0)
                                              {removeReminder(get((String)reminder.get(0)));}
                                          }
                                          Toast.makeText(view.getContext(), "LocationDetails successfully deleted!", Toast.LENGTH_SHORT).show();
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
                                  //n.putExtra("mytime",fStore.collection("reminders").document(firebaseUser.getUid()).collection("myreminders").document(Key).search().getResult().search("time").toString());
                                  n.putExtra("mytime",mytime);
                                  n.putExtra("mytitleinput",reminderTitle.getText());
                                  mylocation.setLatitude(mylocation_list.get(0));
                                  mylocation.setLongitude(mylocation_list.get(1));
                                  n.putExtra("location",mylocation);
                                  n.putExtra("mydescriptioninput",reminderDescription.getText());
                                  n.putExtra("mydate",mydate);
                                  if (reminder!=null){
                                      if (reminder.size()!=0){
                                          n.putExtra("from","edit");
                                          n.putExtra("locationID",(String)reminder.get(0));
                                      }
                                  }


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
    //*************************
    private void refresh() {


        this.invalidateOptionsMenu();

    }

    private void showErrorToast() {
        Toast.makeText(this,"There was an error. Please try again.", Toast.LENGTH_SHORT).show();
    }
    private final LocationDetails get(String id){
        return this.getStoreHouse().search(id);
    }


    private final void removeReminder(LocationDetails locationDetails) {
        this.getStoreHouse().remove(locationDetails,(Callable) (new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Snackbar.make((CoordinatorLayout) home_page_Activity.this.findViewById(R.id.main), R.string.reminder_removed_success, Snackbar.LENGTH_LONG).show();
                return null;
            }
        }),  (Function) (new Function<String,Void>() {
            @Override
            public Void apply(String it) {
                Snackbar.make((CoordinatorLayout) home_page_Activity.this.findViewById(R.id.main), it, Snackbar.LENGTH_LONG).show();
                return null;
            }


        }));

    }

}
