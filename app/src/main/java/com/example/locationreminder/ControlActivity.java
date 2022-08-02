package com.example.locationreminder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.Manifest;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public final class ControlActivity extends SupportActivity implements OnMapReadyCallback{

    //********************************
    Button myok_button;
    String mytitleinput;
    String mydescriptioninput;
    String mydate;
    String mytime;
    String from="";
    static String Key="";
    String locationID;
    //********************************
    private GoogleMap map;
    private LocationManager locationManager;
    private static final String EXTRA_LAT_LNG = "EXTRA_LAT_LNG";
    //public static final MainActivity.Companion Companion = new MainActivity.Companion((DefaultConstructorMarker)null);
    //private Object CameraUpdateFactory;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        findViewById(R.id.newReminder).setVisibility(View.GONE);
        findViewById(R.id.currentLocation).setVisibility(View.GONE);
        ((FloatingActionButton) findViewById(R.id.newReminder)).setOnClickListener((OnClickListener) (new OnClickListener() {
            @Override
            public void onClick(View it) {
                Intent intent = LocationActivity.Companion.newIntent((Context) ControlActivity.this, ControlActivity.this.map.getCameraPosition().target, ControlActivity.this.map.getCameraPosition().zoom);
                intent.putExtra("mytitleinput",mytitleinput);
                intent.putExtra("mydescriptioninput",mydescriptioninput);
                intent.putExtra("mydate",mydate);
                intent.putExtra("mytime",mytime);
                intent.putExtra("Key",Key);
                intent.putExtra("from",from);
                intent.putExtra("locationID",locationID);
                ControlActivity.this.startActivityForResult(intent, 330);
            }

        }));
        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission((Context) this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 329);
        }
         //****************************************************
        myok_button = findViewById(R.id.ok_button);
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
        myok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //save location to firebase

                //location=((MapsFragment) fragment).getLastLocation_();
                Intent n= new Intent(ControlActivity.this, add_reminder.class);
                //n.putExtra("location", location);
                n.putExtra("mytitleinput",mytitleinput);
                // n.putExtra("from","location");
                n.putExtra("mydescriptioninput",mydescriptioninput);
                n.putExtra("mydate",mydate);
                n.putExtra("mytime",mytime);
                n.putExtra("Key",Key);
                n.putExtra("ifLocation","no");
                startActivity(n);
            }
        });
        //******************************************************

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 330 && resultCode == Activity.RESULT_OK) {
            this.viewLocations();
            LocationDetails locationDetails = this.getStoreHouse().getFinal();
            this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(locationDetails != null ? locationDetails.getLatLng() : null, 15.0F));
            Snackbar.make((CoordinatorLayout) this.findViewById(R.id.main), R.string.reminder_added_success, Snackbar.LENGTH_LONG).show();
        }

    }


    private final void centerCamera() {
        if (this.getIntent().getExtras() != null && this.getIntent().getExtras().containsKey(EXTRA_LAT_LNG)) {
            LatLng latLng = (LatLng) (this.getIntent().getExtras() != null ? this.getIntent().getExtras().get(EXTRA_LAT_LNG) : null);
            this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0F));
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 329) {
            this.onMapAndPermissionReady();
        }

    }
    private final void onMapAndPermissionReady() {

        if (this.map != null && ContextCompat.checkSelfPermission((Context) this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            this.map.setMyLocationEnabled(true);
            findViewById(R.id.newReminder).setVisibility(View.VISIBLE);
            findViewById(R.id.currentLocation).setVisibility(View.VISIBLE);
            (findViewById(R.id.currentLocation)).setOnClickListener((OnClickListener) (new OnClickListener() {
                @Override
                public void onClick(View it) {
                    String bestProvider = locationManager.getBestProvider(new Criteria(), false);

                    if (ActivityCompat.checkSelfPermission(ControlActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ControlActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(String.valueOf(bestProvider));
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                        ControlActivity.this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0F));

                    }
                }


            }));
            this.viewLocations();
            this.centerCamera();


        }
    }



    private final void viewLocations() {
        this.map.clear();
        List<LocationDetails> reminders_=this.getStoreHouse().getEachOfLocations();
        for (LocationDetails locationDetails : reminders_) {
            // LocationDetails locationDetails = (LocationDetails) reminders_.iterator().next();
            Services.viewLocation((Context) this, this.map, locationDetails);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        this.map.getUiSettings().setMyLocationButtonEnabled(false);
        this.map.getUiSettings().setMapToolbarEnabled(false);
        //this.map.setOnMarkerClickListener((OnMarkerClickListener) this);
        onMapAndPermissionReady();
    }

   /* public boolean onMarkerClick(Marker marker) {
        LocationDetails reminder = this.getStoreHouse().search((String) marker.getTag());
        if (reminder != null) {
            this.showReminderRemoveAlert(reminder);
        }
        return true;
    }*/

    /*private final void showReminderRemoveAlert(LocationDetails locationDetails) {
        AlertDialog alertDialog = (new AlertDialog.Builder((Context) this)).create();
        alertDialog.setMessage((CharSequence) this.getString(R.string.reminder_removal_alert));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, (CharSequence) this.getString(R.string.reminder_removal_alert_positive),new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ControlActivity.this.removeReminder(locationDetails);
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, (CharSequence) this.getString(R.string.reminder_removal_alert_negative),new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }*/


    /*private final void removeReminder(LocationDetails locationDetails) {
        this.getStoreHouse().remove(locationDetails,(Callable) (new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ControlActivity.this.viewLocations();
                Snackbar.make((CoordinatorLayout) ControlActivity.this.findViewById(R.id.main), R.string.reminder_removed_success, Snackbar.LENGTH_LONG).show();
                return null;
            }
        }),  (Function) (new Function<String,Void>() {
            @Override
            public Void apply(String it) {
                Snackbar.make((CoordinatorLayout) ControlActivity.this.findViewById(R.id.main), it, Snackbar.LENGTH_LONG).show();
                return null;
            }


        }));

    }*/

    public static final class Companion {

        public static final Intent newIntent(Context context, LatLng latLng) {
            Intent intent = new Intent(context, ControlActivity.class);
            intent.putExtra(EXTRA_LAT_LNG, (Parcelable)latLng);
            return intent;
        }

        private Companion() {
        }
    }


}
