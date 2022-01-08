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
import android.widget.Button;
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
public class add_location extends AppCompatActivity {
    Button mycancel_button, myok_button;
    FrameLayout myframe_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        mycancel_button = findViewById(R.id.cancel_button);
        myok_button = findViewById(R.id.ok_button);
        myframe_layout=findViewById(R.id.frame_layout);
        Fragment fragment =new MapsFragment();
        //Open fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).commit();
        mycancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //go to add reminder page on click
                startActivity(new Intent(add_location.this, add_reminder.class));
            }
        });
    }
}
