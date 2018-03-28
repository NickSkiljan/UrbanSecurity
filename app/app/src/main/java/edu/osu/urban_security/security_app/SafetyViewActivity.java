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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SafetyViewActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;

    private FusedLocationProviderClient mFusedLocationClient;
    private Button SOSPushButton;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
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

        t = (TextView)findViewById(R.id.textView);

        //Create intent and start listening service
        Intent intent = new Intent(this, CallDetectionService.class);
        startService(intent);

        SOSPushButton = findViewById(R.id.button_push_sos);

        t.setText("Checking Permissions");

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.CALL_PHONE},
                MY_PERMISSIONS_REQUEST_CALL_PHONE);

        SOSPushButton.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            t.setText("PERMISSION NOT GRANTED");
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                String lat = "Latitude: " + Double.toString(location.getLatitude()) + "\n";
                                String lng = "Longitude: " + Double.toString(location.getLongitude()) + "\n";
                                String alt = "Altitude: " + Double.toString(location.getAltitude()) + "\n";
                                t.setText(lat+lng+alt);
                            }
                        }
                    });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_page, menu);
        return true;
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

    /* Currently triggering send on application resume.
        TODO: Trigger on acutal call
     */
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(OutgoingCallDetector.MY_PREF,MODE_PRIVATE);
        String number = sharedPreferences.getString(OutgoingCallDetector.NUMBER_KEY,"No Value Found");
        t.setText(number);
        Log.d("APP RESUMED", number);
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_push_sos){
            pushSOS();
            initiateCall();
        }
    }

    private void pushSOS() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            t.setText("PERMISSION NOT GRANTED");
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                String lat = "Latitude: " + Double.toString(location.getLatitude()) + "\n";
                                String lng = "Longitude: " + Double.toString(location.getLongitude()) + "\n";
                                String alt = "Altitude: " + Double.toString(location.getAltitude()) + "\n";
                                t.setText(lat+lng+alt);
                            }

                            mDatabase.child("sos").child(user.getUid()).setValue("test-sos");
                            mDatabase.child("users").child(user.getUid()).setValue("test-update");
                        }
                    });
        }
    }

    private void initiateCall(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSION NOT GRANTED", "phone call");
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:16143793483"));
            startActivity(callIntent);
        }

    }
}
