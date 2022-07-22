package com.example.locationreminder;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.appcompat.app.ActionBar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.function.Function;

public final class NewReminderActivity extends BaseActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private Reminder reminder = new Reminder( (LatLng)null, (Double)null, (String)null);

    Button myok_button;
    String mytitleinput;
    String mydescriptioninput;
    String mydate;
    String mytime;
    String from="";
    String locationID;
    static String Key="";
    private final Object radiusBarChangeListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser) {
            NewReminderActivity.this.updateRadiusWithProgress(progress);
            NewReminderActivity.this.showReminderUpdate();
        }
    };
    private static final String EXTRA_LAT_LNG = "EXTRA_LAT_LNG";
    private static final String EXTRA_ZOOM = "EXTRA_ZOOM";
    public static final NewReminderActivity.Companion Companion = new NewReminderActivity.Companion();
    private final void updateRadiusWithProgress(int progress) {
        double radius = this.getRadius(progress);
        this.reminder.setRadius(radius);
        ((TextView)findViewById(R.id.radiusDescription)).setText((CharSequence)this.getString(R.string.radius_description, new Object[]{String.valueOf((int) Math.round((radius)))}));
    }

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_new_reminder);
        SupportMapFragment mapFragment = (SupportMapFragment)this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback)this);
        findViewById(R.id.instructionTitle).setVisibility(View.GONE);
        findViewById(R.id.instructionSubtitle).setVisibility(View.GONE);
        findViewById(R.id.radiusBar).setVisibility(View.GONE);
        findViewById(R.id.radiusDescription).setVisibility(View.GONE);
        findViewById(R.id.message).setVisibility(View.GONE);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mytitleinput = extras.getString("mytitleinput");
            mydescriptioninput = extras.getString("mydescriptioninput");
            mydate = extras.getString("mydate");
            mytime = extras.getString("mytime");
            Key= extras.getString("Key");
            from= extras.getString("from");
            locationID= extras.getString("locationID");

            //The key argument here must match that used in the other activity
        }
    }

    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    public void onMapReady( GoogleMap googleMap) {
        this.map = googleMap;
        this.map.getUiSettings().setMapToolbarEnabled(false);
        this.centerCamera();
        this.showConfigureLocationStep();
    }

    private final void centerCamera() {
        LatLng latLng = (LatLng)this.getIntent().getExtras().get(EXTRA_LAT_LNG);
        float zoom = (Float)this.getIntent().getExtras().get(EXTRA_ZOOM);
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));


    }

    private final void showConfigureLocationStep() {
        findViewById(R.id.marker).setVisibility(View.VISIBLE);
        findViewById(R.id.instructionTitle).setVisibility(View.VISIBLE);
        findViewById(R.id.instructionSubtitle).setVisibility(View.VISIBLE);
        findViewById(R.id.radiusBar).setVisibility( View.GONE);
        findViewById(R.id.radiusDescription).setVisibility( View.GONE);
        findViewById(R.id.message).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.instructionTitle)).setText((CharSequence)this.getString(R.string.instruction_where_description));
        ((Button)this.findViewById(R.id.next)).setOnClickListener((OnClickListener)(new OnClickListener() {
            public final void onClick(View it) {
                NewReminderActivity.this.reminder.setLatLng(map.getCameraPosition().target);
                NewReminderActivity.this.showConfigureRadiusStep();
            }
        }));
        this.showReminderUpdate();
    }

    private final void showConfigureRadiusStep() {
        findViewById(R.id.marker).setVisibility(View.GONE);
        findViewById(R.id.instructionTitle).setVisibility(View.VISIBLE);
        findViewById(R.id.instructionSubtitle).setVisibility(View.GONE);
        findViewById(R.id.radiusBar).setVisibility(View.VISIBLE);
        findViewById(R.id.radiusDescription).setVisibility(View.VISIBLE);
        findViewById(R.id.message).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.instructionTitle)).setText((CharSequence)this.getString(R.string.instruction_radius_description));
        ((Button)this.findViewById(R.id.next)).setOnClickListener((OnClickListener)(new OnClickListener() {
            public final void onClick(View it) {
                NewReminderActivity.this.showConfigureMessageStep();
            }
        }));
        SeekBar radiusBar=findViewById(R.id.radiusBar);
        radiusBar.setOnSeekBarChangeListener((OnSeekBarChangeListener) radiusBarChangeListener);
        this.updateRadiusWithProgress(((SeekBar)findViewById(R.id.radiusBar)).getProgress());
        this.map.animateCamera(CameraUpdateFactory.zoomTo(15.0F));
        this.showReminderUpdate();
    }

    private final double getRadius(int progress) {
        return (double)100 + ((double)2 * (double)progress + (double)1) * (double)100;
    }

    private final void showConfigureMessageStep() {

        findViewById(R.id.marker).setVisibility(View.GONE);
        findViewById(R.id.instructionTitle).setVisibility(View.VISIBLE);
        findViewById(R.id.instructionSubtitle).setVisibility(View.GONE);
        findViewById(R.id.radiusBar).setVisibility(View.GONE);
        findViewById(R.id.radiusDescription).setVisibility(View.GONE);
        findViewById(R.id.message).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.instructionTitle)).setText((CharSequence)this.getString(R.string.instruction_where_description));
        ((Button)this.findViewById(R.id.next)).setOnClickListener((OnClickListener)(new OnClickListener() {
            public final void onClick(View it) {
                Utils.hideKeyboard((Context)NewReminderActivity.this,(EditText)NewReminderActivity.this.findViewById(R.id.message));
                NewReminderActivity.this.reminder.setMessage(((TextView)findViewById(R.id.message)).getText().toString());
                if ((CharSequence)NewReminderActivity.this.reminder.getMessage() == null || NewReminderActivity.this.reminder.getMessage().length() == 0) {
                    ((TextView)findViewById(R.id.message)).setError((CharSequence)NewReminderActivity.this.getString(R.string.error_required));
                } else {
                    //******************************added to translate add to add_reminder -start**********************

                    addReminder2();
//******************************added to translate add to add_reminder -end************************
                    //NewReminderActivity.this.addReminder(NewReminderActivity.this.reminder);


                    Intent n= new Intent(NewReminderActivity.this, add_reminder.class);
                    n.putExtra("mytitleinput",mytitleinput);
                    n.putExtra("mydescriptioninput",mydescriptioninput);
                    n.putExtra("mydate",mydate);
                    n.putExtra("mytime",mytime);
                    n.putExtra("Key",Key);
                    n.putExtra("ifLocation","yes");
                    n.putExtra("locationID",locationID);
                    if(from!=null){if(from.equals("edit")){
                    n.putExtra("from","edited");
                    }else{n.putExtra("from","");}}
                    startActivity(n);
                }

            }
        }));
        Utils.requestFocusWithKeyboard((EditText)this.findViewById(R.id.message));
        this.showReminderUpdate();
    }
    //******************************added to translate add to add_reminder -start**********************
    public final void addReminder2() {
        this.getRepository().saveReminder(NewReminderActivity.this.reminder);
 //this.getRepository().setReminder(NewReminderActivity.this.reminder);
    }

    //******************************added to translate add to add_reminder -end************************
    public final void addReminder(Reminder reminder) {
        this.getRepository().add(reminder,(Callable) (new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                NewReminderActivity.this.setResult(Activity.RESULT_OK);
                NewReminderActivity.this.finish();
                return null;
            }
        }),(Function) (new Function<String,Void>() {
            @Override
            public Void apply(String it) {
                Snackbar.make((CoordinatorLayout)NewReminderActivity.this.findViewById(R.id.main), it, Snackbar.LENGTH_LONG).show();
                return null;
            }
        }));
    }

    private final void showReminderUpdate() {
        this.map.clear();
        Utils.showReminderInMap((Context)this, this.map, this.reminder);
    }



    public static final class Companion {

        public final Intent newIntent( Context context,  LatLng latLng, float zoom) {
            Intent intent = new Intent(context, NewReminderActivity.class);
            intent.putExtra(EXTRA_LAT_LNG, (Parcelable)latLng).putExtra(EXTRA_ZOOM, zoom);
            return intent;
        }

        private Companion() {
        }

    }
}

