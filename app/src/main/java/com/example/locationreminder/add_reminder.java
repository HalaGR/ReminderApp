package com.example.locationreminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.time.Year;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class add_reminder extends AppCompatActivity {

    EditText mytitleinput, mydescriptioninput, mydate,mytime;
    TextView mydate_switch,mytime_switch,mylocation_switch;
    FloatingActionButton mysavebtn;
    SwitchCompat my_weather_switch;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebasefirestore;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    LinearLayout add_weather;
    DatePickerDialog.OnDateSetListener setListener; // listener for choose date
    String[] weather_conditions = {"Thunder Storm", "Strong Rain", "Snow", "Light Rain", "Foggy", "Overcast", "Sunny", "Cloudy"}; // all weather conditions
    AutoCompleteTextView autoCompleteTxt;
    ViewPager myviewPager;
    FrameLayout myframe_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        mytitleinput = findViewById(R.id.titleinput);
        mydescriptioninput = findViewById(R.id.descriptioninput);
        mysavebtn = findViewById(R.id.savebtn);
        mydate = findViewById(R.id.date);
        mytime=findViewById(R.id.time);
        my_weather_switch = findViewById(R.id.weather_switch);
        mydate_switch= findViewById(R.id.date_switch);
        mylocation_switch=findViewById(R.id.location_switch);
        mytime_switch=findViewById(R.id.time_switch);
        myframe_layout=findViewById(R.id.frame_layout);

        // choose date UI
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        mydate_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        add_reminder.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        mydate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        // go to choose weather
        autoCompleteTxt = findViewById(R.id.auto_complete_text);
        add_weather = findViewById(R.id.layout_add_weather); // need to make it visible

        adapterItems = new ArrayAdapter<String>(this,R.layout.weather_list,weather_conditions);
        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
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

        //time set
        final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        mytime_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        add_reminder.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                     Calendar c =Calendar.getInstance();
                     c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                     c.set(Calendar.MINUTE,minute);
                     c.set(Calendar.SECOND,0);
                        String time = hourOfDay + ":" + minute ;
                        mytime.setText(time);

                    }
                }, hourOfDay, minute, false);

                timePickerDialog.show();


            }
        });
       //location set
        //final LocationAdapter adapter=new LocationAdapter(getSupportFragmentManager(),this,1);
        mylocation_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialize fragment
               //Fragment fragment =new MapsFragment();
               //Open fragment
                //getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).commit();
                startActivity(new Intent(add_reminder.this,add_location.class));
            }
        });



        Toolbar toolbar = findViewById(R.id.toolbarofaddreminder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get current user database
        firebaseAuth = FirebaseAuth.getInstance();
        firebasefirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mysavebtn.setOnClickListener(new View.OnClickListener(){// add reminder clicked
            @Override
            public void onClick(View view) {
                String title = mytitleinput.getText().toString();
                String description = mydescriptioninput.getText().toString();
                String date = mydate.getText().toString();
                String time=mytime.getText().toString();
                if (title.isEmpty() || date.isEmpty() || time.isEmpty()){
                    //are all files filed
                    Toast.makeText(getApplicationContext(), "Please fill Both title and Date files", Toast.LENGTH_SHORT).show();
                }else{
                    // add reminder to database
                    DocumentReference documentReference = firebasefirestore.collection("reminders").document(firebaseUser.getUid()).collection("myreminders").document();
                    Map<String, Object> reminder = new HashMap<>();
                    reminder.put("title", title);
                    reminder.put("description", description);
                    reminder.put("date", date);
                    reminder.put("time", time);
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
