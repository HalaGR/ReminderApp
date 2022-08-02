package com.example.locationreminder;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public final class LocationActivity extends SupportActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private LocationDetails locationDetails = new LocationDetails( (LatLng)null, (Double)null, (String)null);

    Button myok_button;
    String mytitleinput;
    String mydescriptioninput;
    String mydate;
    String mytime;
    String from="";
    String locationID;
    static String Key="";
    private static final String EXTRA_LAT_LNG = "EXTRA_LAT_LNG";
    private static final String EXTRA_ZOOM = "EXTRA_ZOOM";
    private final Object radiusBarChangeListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser) {
            LocationActivity.this.refreshRadius(progress);
            LocationActivity.this.updateLocation();
        }
    };

    public static final LocationActivity.Companion Companion = new LocationActivity.Companion();
    private final void refreshRadius(int progress) {
        double radius = this.realRadius(progress);
        this.locationDetails.setRadius(radius);
        ((TextView)findViewById(R.id.radiusDescription)).setText((CharSequence)this.getString(R.string.radius_description1, new Object[]{String.valueOf((int) Math.round((radius)))}));
    }

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_location);
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



    public void onMapReady( GoogleMap googleMap) {
        this.map = googleMap;
        this.map.getUiSettings().setMapToolbarEnabled(false);
        this.centerCamera();
        this.showStepOfLocation();
    }
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    private final void showStepOfRadius() {
        findViewById(R.id.marker).setVisibility(View.GONE);
        findViewById(R.id.instructionTitle).setVisibility(View.VISIBLE);
        findViewById(R.id.instructionSubtitle).setVisibility(View.GONE);
        findViewById(R.id.radiusBar).setVisibility(View.VISIBLE);
        findViewById(R.id.radiusDescription).setVisibility(View.VISIBLE);
        findViewById(R.id.message).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.instructionTitle)).setText((CharSequence)this.getString(R.string.instruction_radius_description));
        ((Button)this.findViewById(R.id.next)).setOnClickListener((OnClickListener)(new OnClickListener() {
            public final void onClick(View it) {
                LocationActivity.this.showStepOfMessage();
            }
        }));
        SeekBar radiusBar=findViewById(R.id.radiusBar);
        radiusBar.setOnSeekBarChangeListener((OnSeekBarChangeListener) radiusBarChangeListener);
        this.refreshRadius(((SeekBar)findViewById(R.id.radiusBar)).getProgress());
        this.map.animateCamera(CameraUpdateFactory.zoomTo(15.0F));
        this.updateLocation();
    }

    private final void showStepOfLocation() {
        findViewById(R.id.marker).setVisibility(View.VISIBLE);
        findViewById(R.id.instructionTitle).setVisibility(View.VISIBLE);
        findViewById(R.id.instructionSubtitle).setVisibility(View.VISIBLE);
        findViewById(R.id.radiusBar).setVisibility( View.GONE);
        findViewById(R.id.radiusDescription).setVisibility( View.GONE);
        findViewById(R.id.message).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.instructionTitle)).setText((CharSequence)this.getString(R.string.instruction_where_description1));
        ((Button)this.findViewById(R.id.next)).setOnClickListener((OnClickListener)(new OnClickListener() {
            public final void onClick(View it) {
                LocationActivity.this.locationDetails.setLatLng(map.getCameraPosition().target);
                LocationActivity.this.showStepOfRadius();
            }
        }));
        this.updateLocation();
    }
    private final void centerCamera() {
        LatLng latLng = (LatLng)this.getIntent().getExtras().get(EXTRA_LAT_LNG);
        float zoom = (Float)this.getIntent().getExtras().get(EXTRA_ZOOM);
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));


    }



    private final double realRadius(int progress) {
        return (double)100 + ((double)2 * (double)progress + (double)1) * (double)100;
    }

    private final void showStepOfMessage() {

        findViewById(R.id.marker).setVisibility(View.GONE);
        findViewById(R.id.instructionTitle).setVisibility(View.VISIBLE);
        findViewById(R.id.instructionSubtitle).setVisibility(View.GONE);
        findViewById(R.id.radiusBar).setVisibility(View.GONE);
        findViewById(R.id.radiusDescription).setVisibility(View.GONE);
        findViewById(R.id.message).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.instructionTitle)).setText((CharSequence)this.getString(R.string.instruction_where_description1));
        ((Button)this.findViewById(R.id.next)).setOnClickListener((OnClickListener)(new OnClickListener() {
            public final void onClick(View it) {
                Services.coverConsole((Context) LocationActivity.this,(EditText) LocationActivity.this.findViewById(R.id.message));
                LocationActivity.this.locationDetails.setMessage(((TextView)findViewById(R.id.message)).getText().toString());
                if ((CharSequence) LocationActivity.this.locationDetails.getMessage() == null || LocationActivity.this.locationDetails.getMessage().length() == 0) {
                    ((TextView)findViewById(R.id.message)).setError((CharSequence) LocationActivity.this.getString(R.string.error_required));
                } else {
                    //******************************added to translate add to add_reminder -start**********************

                    saveLocation();
//******************************added to translate add to add_reminder -end************************
                    //LocationActivity.this.addReminder(LocationActivity.this.locationDetails);


                    Intent n= new Intent(LocationActivity.this, add_reminder.class);
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
        Services.FocusingWithConsole((EditText)this.findViewById(R.id.message));
        this.updateLocation();
    }
    //******************************added to translate add to add_reminder -start**********************
    public final void saveLocation() {
        this.getStoreHouse().saveLocation(LocationActivity.this.locationDetails);
 //this.getStoreHouse().setReminder(LocationActivity.this.locationDetails);
    }

 /*   //******************************added to translate add to add_reminder -end************************
    public final void addReminder(LocationDetails locationDetails) {
        this.getStoreHouse().add(locationDetails,(Callable) (new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                LocationActivity.this.setResult(Activity.RESULT_OK);
                LocationActivity.this.finish();
                return null;
            }
        }),(Function) (new Function<String,Void>() {
            @Override
            public Void apply(String it) {
                Snackbar.make((CoordinatorLayout) LocationActivity.this.findViewById(R.id.main), it, Snackbar.LENGTH_LONG).show();
                return null;
            }
        }));
    }*/

    private final void updateLocation() {
        this.map.clear();
        Services.viewLocation((Context)this, this.map, this.locationDetails);
    }



    public static final class Companion {

        public final Intent newIntent( Context context,  LatLng latLng, float zoom) {
            Intent intent = new Intent(context, LocationActivity.class);
            intent.putExtra(EXTRA_LAT_LNG, (Parcelable)latLng).putExtra(EXTRA_ZOOM, zoom);
            return intent;
        }

        private Companion() {
        }

    }
}

