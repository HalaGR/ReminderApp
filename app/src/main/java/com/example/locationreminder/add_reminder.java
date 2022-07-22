package com.example.locationreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
//import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.gms.common.internal.ICancelToken;
//import com.example.locationreminder.databinding.ActivityMainBinding;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;


public class add_reminder extends BaseActivity {


    EditText mytitleinput, mydescriptioninput, mydate,mytime;
    SwitchCompat mydate_switch,mytime_switch,mylocation_switch,remindmethere_switch,my_weather_switch;
    EditText  weatherCity;
    SwitchCompat myTime_switch;
    FloatingActionButton mysavebtn;
    SwitchCompat my_location_switch;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebasefirestore;
    ArrayAdapter<String> adapterItems;
    LinearLayout add_weather;
    DatePickerDialog.OnDateSetListener setListener; // listener for choose date
    String[] weather_conditions = {"Thunder Storm", "Strong Rain", "Snow", "Light Rain", "Foggy", "Overcast", "Sunny", "Cloudy"}; // all weather conditions
    AutoCompleteTextView autoCompleteTxt;
    FrameLayout myframe_layout;

    Location location=new Location("");
    String title;
    String description ;
    String date ;
    String time;
    String city ;
    String ifLocation;
    String condition ;
    String Key="";
    String from="";
    String locationID="";

    Button timeButten,exit;
    TextInputLayout weatherCond;
    int hour, minute;
    //set alarm param
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar c;
//********************8
    private String id;
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
        my_weather_switch = findViewById(R.id.weather_switch);
        mydate_switch= findViewById(R.id.date_switch);
        mytime=findViewById(R.id.time);
        mylocation_switch=findViewById(R.id.location_switch);
        remindmethere_switch=findViewById(R.id.remindmethere_switch);
        mytime_switch=findViewById(R.id.time_switch);
        timeButten = findViewById(R.id.timeButten);
        myframe_layout=findViewById(R.id.frame_layout);
        weatherCity = findViewById(R.id.weather_city);
        weatherCond = findViewById(R.id.weather_cond);
        myTime_switch = findViewById(R.id.time_switch);
        Log.d("notification", "inside  addReminder#1");
        // choose date UI
        c = Calendar.getInstance();
        createNotificationChannel();
        final int year = c.get(Calendar.YEAR);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int month = c.get(Calendar.MONTH);
        // go to choose weather
        autoCompleteTxt = findViewById(R.id.auto_complete_text);
        add_weather = findViewById(R.id.layout_add_weather); // need to make it visible
//**********************************************
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            location = extras.getParcelable("location");
            title = extras.getString("mytitleinput");
            description = extras.getString("mydescriptioninput");
            date = extras.getString("mydate");
            time = extras.getString("mytime");
            city= extras.getString("mycity");
            condition= extras.getString("mycondition");
            Key= extras.getString("Key");
            ifLocation= extras.getString("ifLocation");
            from= extras.getString("from");
            locationID= extras.getString("locationID");

            //******************************************************
           // id =extras.getString("id");
           // latLng=(LatLng)extras.get("LatLng");
           // radius= extras.getDouble("Radius");
           // message= extras.getString("Message");
            //*******************************************************
            mytitleinput.setText(title);
            mydescriptioninput.setText(description);
            mydate.setText(date);
            timeButten.setText(time);
            mytime.setText(time);
            weatherCity.setText(city);
            autoCompleteTxt.setText(condition);
          if(date!=null) {if(!date.equals("")) mydate_switch.setChecked(true);}
            if(city!=null){ if(!city.equals("") && !condition.equals("")) {my_weather_switch.setChecked(true); add_weather.setVisibility(LinearLayout.VISIBLE);}}
            if(time!=null) {if(!time.equals("")) mytime_switch.setChecked(true);}
             if(location!=null){ if(location.getLatitude()!=0.0&&location.getLongitude()!=0.0)mylocation_switch.setChecked(true);}
            if(ifLocation!=null){if(ifLocation.equals("yes"))remindmethere_switch.setChecked(true);}
           if (locationID!=null){ if (!locationID.equals("")){remindmethere_switch.setChecked(true);}}
            //The key argument here must match that used in the other activity
        }
//**************************************************

        adapterItems = new ArrayAdapter<String>(this,R.layout.weather_list,weather_conditions);
        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Condition: "+item,Toast.LENGTH_SHORT).show();
            }
        });
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


        my_weather_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked == true){
                    Toast.makeText(getBaseContext(), "On", Toast.LENGTH_SHORT).show();
                    add_weather.setVisibility(LinearLayout.VISIBLE);

                }else{
                    Toast.makeText(getBaseContext(), "Off", Toast.LENGTH_SHORT).show();
                    add_weather.setVisibility(LinearLayout.INVISIBLE);

                }
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
        mytime_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked == true){
                    Toast.makeText(getBaseContext(), "On", Toast.LENGTH_SHORT).show();
                    timeButten.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getBaseContext(), "Off", Toast.LENGTH_SHORT).show();
                    timeButten.setText("Select Time");
                    mytime.setText("");
                    timeButten.setVisibility(View.INVISIBLE);

                }
            }
        });

        // Time switch
        //time set
        Calendar calendar = Calendar.getInstance();
        final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        mytime_switch.setOnClickListener(new View.OnClickListener() {
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
        });


      /*  timeButten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        add_reminder.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
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
        mylocation_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //Initialize fragment
               //Fragment fragment =new MapsFragment();
               //Open fragment
                //getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).commit();
                if (isChecked == true) {
                    Intent n = new Intent(add_reminder.this, add_location.class);
                    if (mytitleinput.getText() != null) {
                        n.putExtra("mytitleinput", mytitleinput.getText().toString());
                    }
                    if (mydescriptioninput.getText() != null) {
                        n.putExtra("mydescriptioninput", mydescriptioninput.getText().toString());
                    }
                    if (mydate.getText() != null) {
                        n.putExtra("mydate", mydate.getText().toString());
                    }
                    if (timeButten.getText() != null) {
                        n.putExtra("mytime", mytime.getText().toString());
                    }
                if ( weatherCity.getText() != null) {
                    n.putExtra("mycity",  weatherCity.getText().toString());
                }
                if (autoCompleteTxt.getText() != null) {
                    n.putExtra("mycondition",autoCompleteTxt.getText().toString());
                }
                 n.putExtra("location",location);
                   n.putExtra("Key",Key);
                    startActivity(n);
                }else{
                    Toast.makeText(getBaseContext(), "Off", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                    if (timeButten.getText() != null) {
                        n.putExtra("mytime", mytime.getText().toString());
                    }
                    if ( weatherCity.getText() != null) {
                        n.putExtra("mycity",  weatherCity.getText().toString());
                    }
                    if (autoCompleteTxt.getText() != null) {
                        n.putExtra("mycondition",autoCompleteTxt.getText().toString());
                    }
                    n.putExtra("location",location);
                    n.putExtra("Key",Key);
                    n.putExtra("from",from);
                    n.putExtra("locationID",locationID);
                    startActivity(n);
                }else{
                    Toast.makeText(getBaseContext(), "Off", Toast.LENGTH_SHORT).show();
                }
            }
        });




        Toolbar toolbar = findViewById(R.id.toolbarofaddreminder);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get current user database
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
                String city = weatherCity.getText().toString();
                String condition = autoCompleteTxt.getText().toString();
                //new  NewReminderActivity().addReminder(new Reminder(id,latLng,radius,message));


                // add reminder to database
                DocumentReference documentReference = firebasefirestore.collection("reminders").document(firebaseUser.getUid()).collection("myreminders").document();
                Map<String, Object> reminder = new HashMap<>(); // save reminder data in a map

                List<Object> reminderLocation_list=new ArrayList<Object>();
                if (remindmethere_switch.isChecked()) {
                    Reminder reminder2=getLast();
                // List<Object> reminderLocation_list=new ArrayList<Object>();
                    if(!locationID.equals("")){
                        reminder2=get(locationID);
                    }else{addReminder();}
                     if(from.equals("edited")){
                        removeReminder(get(locationID));
                         reminder2=getLast();
                         addReminder();}
                reminderLocation_list.add(reminder2.getId());
                reminderLocation_list.add(reminder2.getLatLng());
                reminderLocation_list.add(reminder2.getRadius());
                reminderLocation_list.add(reminder2.getMessage());

                reminder.put("reminder",reminderLocation_list);
                }else{
                    reminder.put("reminder",reminderLocation_list);
                }
                //reminder.put("reminder",getLast());
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
                if (my_weather_switch.isChecked()) {   //if weather condition is chosen and a city
                    if (city.isEmpty() || condition.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please fill Both City and Weather Condition files", Toast.LENGTH_SHORT).show();
                    } else {
                        List<String> weather=new ArrayList<String>();
                        weather.add(city);
                        weather.add(condition);
                        reminder.put("weather",weather);
                    }
                }else{
                    List<String> weather=new ArrayList<String>();
                    reminder.put("weather",weather);

                }
               if (mylocation_switch.isChecked()){ // Need to fill this for location
                List<Double> location_list=new ArrayList<Double>();
                location_list.add(location.getLatitude());
                location_list.add(location.getLongitude());
                //*******************
                 //  Intent n = new Intent(add_reminder.this, ControlActivityForDelete.class);
                //   n.putExtra("getLatitude",location.getLatitude());
                //   n.putExtra("getLongitude",location.getLongitude());
                //   startActivity(n);

               // *********************
                reminder.put("location",location_list);

               }else {
                   List<Double> location_list=new ArrayList<Double>();
                   reminder.put("location",location_list);
               }
                if (title.isEmpty()||description.isEmpty() || (!(mydate_switch.isChecked()) && !(mytime_switch.isChecked()) &&!(mylocation_switch.isChecked())&&!(remindmethere_switch.isChecked()) && !(my_weather_switch.isChecked()))){// make sure files are filled
                    //are all files filed
                    Toast.makeText(getApplicationContext(), "Please fill Both title , description files and one of reminder ways", Toast.LENGTH_SHORT).show();
                }else {
                    reminder.put("title", title);
                    reminder.put("description", description);
                   if(Key!=null){ if (Key.equals("")) {
                        documentReference.set(reminder).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //reminder saved successfully
                                // send feedback and go back to home page
                                Toast.makeText(getApplicationContext(), "Reminder saved successfully, we'll remind you!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(add_reminder.this, home_page_Activity.class));
                                //set alarM
                                setAlarm();
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
                                setAlarm();
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
        this.getRepository().add(this.getRepository().getLast(),(Callable) (new Callable<Void>() {
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
    public final Reminder getLast() {
    return this.getRepository().getLast();}


    private final Reminder get(String id){
        return this.getRepository().get(id);
    }


    private final void removeReminder(Reminder reminder) {
        this.getRepository().remove(reminder,(Callable) (new Callable<Void>() {
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
    //******************************added to translate add-remove to add_reminder -end**********************

    private void setAlarm() {
        Log.d("notification", "inside setAlarm in addReminder" );

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(this, "Alarm set Successfully" , Toast.LENGTH_SHORT).show();


       /* //Snooze button
        Intent snoozeIntent = new Intent(this, AlarmReceiver.class);
       // snoozeIntent.setAction("Snooze");
        snoozeIntent.putExtra("Snooze", 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(this, 0, snoozeIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "foxandroid")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.snooze_icon, "Snooze",
                        snoozePendingIntent);*/

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
            Log.d("notification", "inside createNotificationChannel in addReminder");

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
                timeButten.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }

    }
