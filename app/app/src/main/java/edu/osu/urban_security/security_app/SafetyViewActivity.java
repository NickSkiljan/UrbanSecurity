package edu.osu.urban_security.security_app;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;

import edu.osu.urban_security.security_app.models.Globals;
import edu.osu.urban_security.security_app.models.User;

public class SafetyViewActivity extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;
    public static final int PERMISSIONS_CODE = 42;

    private static final String TAG = "SafetyViewActivity";

    private FusedLocationProviderClient mFusedLocationClient;
    private Task<Location> mLastLocation;
    private Button SOSPushButton;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Globals g;
    private TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_view);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        g = Globals.getInstance();

        t = (TextView) findViewById(R.id.textView);

        SOSPushButton = findViewById(R.id.button_push_sos);

//        ActivityCompat.requestPermissions(this,
//                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
//
//        ActivityCompat.requestPermissions(this,
//                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//
//        ActivityCompat.requestPermissions(this,
//                new String[]{android.Manifest.permission.CALL_PHONE},
//                MY_PERMISSIONS_REQUEST_CALL_PHONE);
        String[] permissions = {Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_CODE);

//        SOSPushButton.setOnClickListener(this);
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        //Create intent and start listening service
//        Intent intent = new Intent(this, CallDetectionService.class);
//        startService(intent);
//
//        updateUserLocation();

    }

    private void finishOnCreate() {
        SOSPushButton.setOnClickListener(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Create intent and start listening service
        Intent intent = new Intent(this, CallDetectionService.class);
        startService(intent);

        updateUserLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_page, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_CODE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: PERMISSION GRANTED");
                    finishOnCreate();
                    // PERMISSION GRANTED
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mLastLocation = mFusedLocationClient.getLastLocation();
                    }
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: PERMISSION DENIED");
                    // PERMISSION DENIED
                    t.setText("Location Services Disabled");
                }
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options:
                Intent intent_1 = new Intent(this,OptionsActivity.class);

                startActivity(intent_1);
                overridePendingTransition(R.anim.enter1,R.anim.exit1);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_push_sos){
            updateUserLocation();
            g.pushSOS();
            initiateCall();
        }
    }

    private void initiateCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "initiateCall: PHONE_CALL PERMISSION NOT GRANTED");
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:16143793483"));
            startActivity(callIntent);
        }
    }

    private void updateUserLocation(){
        boolean permissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if(permissionGranted) {
            Log.d(TAG, "updateUserLocation: Permission was granted...");
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            User user = g.user;
                            if (location != null && user != null) {
                                Log.d(TAG, "onSuccess: Location found...");
                                String lat = Double.toString(location.getLatitude());
                                String lng = Double.toString(location.getLongitude());
                                String alt = Double.toString(location.getAltitude());
                                String latString = "Latitude: " + lat + "\n";
                                String lngString = "Longitude: " + lng + "\n";
                                String altString = "Altitude: " + alt + "\n";
                                user.latitude = lat;
                                user.longitude = lng;
                                user.altitude = alt;
                                mDatabase.child("users").child(mAuth.getUid()).setValue(user);
                            } else {
                                Log.d(TAG, "onSuccess: Location or user null. Try opening Google Maps and then try pushing the button again.");
                            }
                        }
                    });

        } else {
            Log.d(TAG, "updateUserLocation: Location Permission was denied...");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_CODE);
            updateUserLocation();
        }
    }
}
