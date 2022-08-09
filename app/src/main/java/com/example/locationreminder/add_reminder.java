package com.example.locationreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
//import android.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
//import com.example.locationreminder.databinding.ActivityMainBinding;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;


public class add_reminder extends SupportActivity {

/* take as input detailes for new reminder, title and description as mandatory, time date and location as optional,
     save new reminders in database and set an alarm based on the input,
     when reminder is saved go back to home page.
   this activity is related to activity_add_reminder.xml
 */
    EditText mytitleinput, mydescriptioninput, mydate,mytime;
    SwitchCompat mydate_switch,mytime_switch,remindmethere_switch;
    SwitchCompat myTime_switch;
    FloatingActionButton mysavebtn;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebasefirestore;
    ArrayAdapter<String> adapterItems;
    DatePickerDialog.OnDateSetListener setListener; // listener for choose date
    FrameLayout myframe_layout;

    String title;
    String description ;
    String date ;
    String time;
    String ifLocation;
    String Key="";
    String from="";
    String locationID="";
    Button exit;
    String is_new_reminder = "";
    int hour, minute;
    //set alarm param
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar c;
//********************8
    private int REQUEST_CODE;
    private LatLng latLng;
    private Double radius;
    private String message;
    //**********************8





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        exit=findViewById(R.id.exit);
        mytitleinput = findViewById(R.id.titleinput);
        mydescriptioninput = findViewById(R.id.descriptioninput);
        mysavebtn = findViewById(R.id.savebtn);
        mydate = findViewById(R.id.date);

        mydate_switch= findViewById(R.id.date_switch);
        mytime=findViewById(R.id.time);
        remindmethere_switch=findViewById(R.id.remindmethere_switch);
        mytime_switch=findViewById(R.id.time_switch);
        myframe_layout=findViewById(R.id.frame_layout);

        myTime_switch = findViewById(R.id.time_switch);
        Log.d("notification", "inside  addReminder#1");
        // choose date UI
        c = Calendar.getInstance();
        createNotificationChannel();
        final int year = c.get(Calendar.YEAR);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int month = c.get(Calendar.MONTH);
//**********************************************
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title = extras.getString("mytitleinput");
            description = extras.getString("mydescriptioninput");
            date = extras.getString("mydate");
            time = extras.getString("mytime");
            Key= extras.getString("Key");
            if(Key==null)Key="";
            ifLocation= extras.getString("ifLocation");
            from= extras.getString("from");
            locationID= extras.getString("locationID");
            REQUEST_CODE= extras.getInt("REQUEST_CODE");
            is_new_reminder = extras.getString("new_reminder");
            mytitleinput.setText(title);
            mydescriptioninput.setText(description);
            mydate.setText(date);
            mytime.setText(time);
          if(date!=null) {if(!date.equals("")) mydate_switch.setChecked(true);}
            if(time!=null) {if(!time.equals("")) mytime_switch.setChecked(true);}
            if(ifLocation!=null){if(ifLocation.equals("yes"))remindmethere_switch.setChecked(true);}
           if (locationID!=null){ if (!locationID.equals("")){remindmethere_switch.setChecked(true);}}
            //The key argument here must match that used in the other activity
        }
//**************************************************

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(add_reminder.this,home_page_Activity.class));
            }
        });
        mydate_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        add_reminder.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        c.set(Calendar.MONTH+1,month);
                        c.set(Calendar.YEAR,year);
                        c.set(Calendar.DAY_OF_MONTH,day);
                        c.set(Calendar.SECOND,0);
                        c.set(Calendar.MILLISECOND,0);
                        mydate.setText(date);
                        mydate_switch.setChecked(true);
                    }

                }, year, month, day);
                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mydate.setText("");
                        mydate_switch.setChecked(false);
                    }
                });
                datePickerDialog.show();
            }
        });


        // Date switch
        mydate_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked == true){
                    Toast.makeText(getBaseContext(), "On", Toast.LENGTH_SHORT).show();
                    mydate.setVisibility(LinearLayout.VISIBLE);
                }else{
                    Toast.makeText(getBaseContext(), "Off", Toast.LENGTH_SHORT).show();
                    mydate.setText("");
                    mydate.setVisibility(LinearLayout.INVISIBLE);

                }
            }
        });
        // Time switch
        //time set
        Calendar calendar = Calendar.getInstance();
        final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        // Time switch
        mytime_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked == true){
                    Toast.makeText(getBaseContext(), "On", Toast.LENGTH_SHORT).show();
                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            add_reminder.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            //Calendar c =Calendar.getInstance();
                            c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                            c.set(Calendar.MINUTE,minute);
                            c.set(Calendar.SECOND,0);
                            String time = hourOfDay + ":" + minute ;
                            mytime.setText(time);
                            mytime_switch.setChecked(true);

                        }
                    }, hourOfDay, minute, false);


                    timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            if (which == DialogInterface.BUTTON_NEGATIVE)
                            {
                                mytime.setText("");
                                mytime_switch.setChecked(false);
                            }
                        }
                    });
                    timePickerDialog.show();


                }else{
                    Toast.makeText(getBaseContext(), "Off", Toast.LENGTH_SHORT).show();
                    mytime.setText("");

                }
            }
        });



        /*mytime_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        add_reminder.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        //Calendar c =Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c.set(Calendar.MINUTE,minute);
                        c.set(Calendar.SECOND,0);
                        String time = hourOfDay + ":" + minute ;
                        mytime.setText(time);
                        mytime_switch.setChecked(true);

                    }
                }, hourOfDay, minute, false);


                timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == DialogInterface.BUTTON_NEGATIVE)
                        {
                            mytime.setText("");
                            mytime_switch.setChecked(false);
                        }
                    }
                });
                timePickerDialog.show();




            }
        });*/



       //location set
        //final LocationAdapter adapter=new LocationAdapter(getSupportFragmentManager(),this,1);

        remindmethere_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //Initialize fragment
                //Fragment fragment =new MapsFragment();
                //Open fragment
                //getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).commit();

                if (isChecked == true) {
                    Intent n = new Intent(add_reminder.this, ControlActivity.class);
                    if (mytitleinput.getText() != null) {
                        n.putExtra("mytitleinput", mytitleinput.getText().toString());
                    }
                    if (mydescriptioninput.getText() != null) {
                        n.putExtra("mydescriptioninput", mydescriptioninput.getText().toString());
                    }
                    if (mydate.getText() != null) {
                        n.putExtra("mydate", mydate.getText().toString());
                    }
                    if (mytime.getText() != null) {
                        n.putExtra("mytime", mytime.getText().toString());
                    }
                    n.putExtra("Key",Key);
                    n.putExtra("from",from);
                    n.putExtra("locationID",locationID);
                    n.putExtra("REQUEST_CODE", REQUEST_CODE);
                    startActivity(n);
                }else{
                    Toast.makeText(getBaseContext(), "Off", Toast.LENGTH_SHORT).show();
                }
            }
        });




        Toolbar toolbar = findViewById(R.id.toolbarofaddreminder);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //search current user database
        firebaseAuth = FirebaseAuth.getInstance();
        firebasefirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mysavebtn.setOnClickListener(new View.OnClickListener(){      // save button clicked
            @Override
            public void onClick(View view) {

                String title = mytitleinput.getText().toString();
                String description = mydescriptioninput.getText().toString();
                String date = mydate.getText().toString();
                String time = mytime.getText().toString();
                //new  LocationActivity().addReminder(new LocationDetails(id,latLng,radius,message));


                // add reminder to database
                DocumentReference documentReference = firebasefirestore.collection("reminders").document(firebaseUser.getUid()).collection("myreminders").document();
                Map<String, Object> reminder = new HashMap<>(); // save reminder data in a map
                List<Object> reminderLocation_list=new ArrayList<Object>();
                if (remindmethere_switch.isChecked())
                {
                    LocationDetails locationDetails2 =getLast();
                // List<Object> reminderLocation_list=new ArrayList<Object>();
                    if(locationID!=null)
                    {
                        if(!locationID.equals(""))
                    {
                        locationDetails2 =get(locationID);
                    }
                    }
                       if(from!=null&&!from.equals("edited")&&!from.equals("edit"))
                    {
                        addReminder();
                    }
                     if(from!=null&&from.equals("edited"))
                     {
                        removeReminder(get(locationID));
                         locationDetails2 =getLast();
                         addReminder();
                     }

                reminderLocation_list.add(locationDetails2.getId());
                reminderLocation_list.add(locationDetails2.getLatLng());
                reminderLocation_list.add(locationDetails2.getRadius());
                reminderLocation_list.add(locationDetails2.getMessage());

                reminder.put("reminder",reminderLocation_list);
                }
                else{
                    reminder.put("reminder",reminderLocation_list);
                }
                //reminder.put("reminder",getFinal());
                if(is_new_reminder != null) {
                    if (is_new_reminder.equals("true")) {
                        REQUEST_CODE = 0;
                    }else if(is_new_reminder.equals("false")){
                        REQUEST_CODE++;
                    }
                }
                if (mydate_switch.isChecked()) { // if Date is chosen
                    if (date.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Choose a Date", Toast.LENGTH_SHORT).show();
                    } else {
                        reminder.put("date", date);
                    }
                }
                else{reminder.put("date","");}
                if(myTime_switch.isChecked()) {    //if time is chosen
                    if (time.equals("Select Time")) {
                        Toast.makeText(getApplicationContext(), "Please Choose a Time", Toast.LENGTH_SHORT).show();
                    } else {
                        reminder.put("time", time);
                    }
                }else{ reminder.put("time", "");}

                if (title.isEmpty()||description.isEmpty()){// make sure files are filled
                    //are all files filed
                    Toast.makeText(getApplicationContext(), "Please fill Both title , description files", Toast.LENGTH_SHORT).show();
                }else {
                    reminder.put("title", title);
                    reminder.put("description", description);
                    reminder.put("ID", REQUEST_CODE);
                   if(Key!=null){ if (Key.equals("")) {
                        documentReference.set(reminder).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //reminder saved successfully
                                // send feedback and go back to home page
                                Toast.makeText(getApplicationContext(), "Location Details saved successfully, we'll remind you!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(add_reminder.this, home_page_Activity.class));
                                //set alarM
                                if(mydate_switch.isChecked() || mytime_switch.isChecked()) {
                                    setAlarm();
                                }
                                //clear input
                                mytitleinput.setText("");
                                mydescriptioninput.setText("");
                                mydate.setText("");
                                mytime.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Something went wrong, Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        firebasefirestore.collection("reminders").document(firebaseUser.getUid()).collection("myreminders").document(Key).update(reminder).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //update saved successfully
                                // send feedback and go back to home page
                                Toast.makeText(getApplicationContext(), "Update saved successfully, we'll remind you!", Toast.LENGTH_LONG).show();

                                startActivity(new Intent(add_reminder.this, home_page_Activity.class));
                                //set alarM
                                if(mydate_switch.isChecked() || mytime_switch.isChecked()) {
                                    setAlarm();
                                }
                                //clear input
                                mytitleinput.setText("");
                                mydescriptioninput.setText("");
                                mydate.setText("");
                                mytime.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Something went wrong, Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
                }
            }
        });

    }
    //******************************added to translate add-remove to add_reminder -start**********************
    public final void addReminder() {
        this.getStoreHouse().getFinal().setTitle(mytitleinput.getText().toString());
        this.getStoreHouse().getFinal().setDescription(mydescriptioninput.getText().toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.getStoreHouse().add(this.getStoreHouse().getFinal(),(Callable) (new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    add_reminder.this.setResult(Activity.RESULT_OK);
                    add_reminder.this.finish();
                    return null;
                }
            }),(Function) (new Function<String,Void>() {
                @Override
                public Void apply(String it) {
                    Snackbar.make((CoordinatorLayout)add_reminder.this.findViewById(R.id.main), it, Snackbar.LENGTH_LONG).show();
                    return null;
                }
            }));
        }
    }
    public final LocationDetails getLast() {
    return this.getStoreHouse().getFinal();}


    private final LocationDetails get(String id){
        return this.getStoreHouse().search(id);
    }


    private final void removeReminder(LocationDetails locationDetails) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            this.getStoreHouse().remove(locationDetails,(Callable) (new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    Snackbar.make((CoordinatorLayout) add_reminder.this.findViewById(R.id.main), R.string.reminder_removed_success, Snackbar.LENGTH_LONG).show();
                    return null;
                }
            }),  (Function) (new Function<String,Void>() {
                @Override
                public Void apply(String it) {
                    Snackbar.make((CoordinatorLayout) add_reminder.this.findViewById(R.id.main), it, Snackbar.LENGTH_LONG).show();
                    return null;
                }


            }));
        }

    }
    //******************************added to translate add-remove to add_reminder -end**********************

    private void setAlarm() {
        //Log.d("notification", "inside setAlarm in addReminder" );
       // if(from != null&&!from.equals("edit")) {
            //new alarm
          /*  alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("title", mytitleinput.getText().toString());
            intent.putExtra("description", mydescriptioninput.getText().toString());
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);*/
       // }else{
            //coming from edit butten
            //cancel already existing alarm
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
          //  alarmManager.cancel(pendingIntent);
            //create new one
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("title", mytitleinput.getText().toString());
            intent.putExtra("description", mydescriptioninput.getText().toString());
            intent.putExtra("REQUEST_CODE", REQUEST_CODE);
            pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
       // }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Alarm set Successfully" , Toast.LENGTH_SHORT).show();
    }


   private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "foxandroidReminderChannel";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("foxandroid",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            //Log.d("notification", "inside createNotificationChannel in addReminder");

        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            // add a back arrow in toolbar
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    public void popTimeClicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                c.set(Calendar.HOUR_OF_DAY,selectedHour);
                c.set(Calendar.MINUTE,minute);
                c.set(Calendar.SECOND,0);
                c.set(Calendar.MILLISECOND,0);
                mytime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }

    }
