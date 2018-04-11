package edu.osu.urban_security.security_app;

import android.*;
import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
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

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import edu.osu.urban_security.security_app.models.Globals;
import edu.osu.urban_security.security_app.models.User;

public class SafetyViewActivity extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;
    public static final int PERMISSIONS_CODE = 42;
    public static final String[] permissions = {Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private static final String TAG = "SafetyViewActivity";
    private static boolean finishedOnCreate = false;

    private FusedLocationProviderClient mFusedLocationClient;
    private Task<Location> mLastLocation;
    private Button SOSPushButton;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Globals g;
    private TextView t;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        sharedPref= getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String username=sharedPref.getString("username","");

        setContentView(R.layout.activity_safety_view);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(username);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        g = Globals.getInstance();

        t = (TextView) findViewById(R.id.textView);
        // Get the first name if the user provided first and last name
        String firstName = username;
        if(firstName.contains(" ")){
            firstName = firstName.substring(0, firstName.indexOf(" "));
        }
        t.setText(firstName+", you are safe!");

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
//        String[] permissions = {Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
//
//        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_CODE);
        requestPermissions();
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

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_CODE);
    }

    private void finishOnCreate() {
        SOSPushButton.setOnClickListener(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Create intent and start listening service
        Intent intent = new Intent(this, CallDetectionService.class);
        startService(intent);

        updateUserLocation();
        finishedOnCreate = true;
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
                    if (!finishedOnCreate) {
                        finishOnCreate();
                    }
                    // PERMISSION GRANTED
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && mFusedLocationClient != null) {
                        mLastLocation = mFusedLocationClient.getLastLocation();
                    } else {
                        finishOnCreate();
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
            case R.id.about:
                Intent intent_1 = new Intent(this,OptionsActivity.class);

                startActivity(intent_1);
                overridePendingTransition(R.anim.enter1,R.anim.exit1);
                return true;

            case R.id.sign_out:
                signOut();
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

                                // RSA key to decrypt user's AES_key
                                try {
                                    // Decode user's Base64-encoded AES_key
//                                    RSA encryption = new RSA();
//                                    byte[] AESKey = encryption.decrypt(user.key));
                                    // Convert key back to SecretKey
                                    byte[] AESKey = user.key.getBytes();
                                    SecretKey key = new SecretKeySpec(AESKey, 0, AESKey.length, "AES");
                                    // Encrypt GeoLocation
//                                    byte[] encryptedLat = AES.encrypt(key, lat.getBytes());
//                                    byte[] encryptedLng = AES.encrypt(key, lng.getBytes());
//                                    byte[] encryptedAlt = AES.encrypt(key, alt.getBytes());
                                    user.latitude = AES.encryptToString(key, lat.getBytes());
                                    user.longitude = AES.encryptToString(key, lng.getBytes());
                                    user.altitude = AES.encryptToString(key, alt.getBytes());
                                    /* [TEST] Manually test decryption locally */
                                    Log.d(TAG, "onSuccess: Pushing encrypted location to Firebase...");
                                    Log.d(TAG, "onSuccess: Latitude = " + new String(AES.decryptString(key, user.latitude)) );
                                    Log.d(TAG, "onSuccess: Longitude = " + new String(AES.decryptString(key, user.longitude)) );
                                    Log.d(TAG, "onSuccess: Altitude = " + new String(AES.decryptString(key, user.altitude)) );
                                    /* [END TEST] */
                                    // Push user information to Firebase
                                    mDatabase.child("users").child(mAuth.getUid()).setValue(user);
                                } catch (Exception e) {
                                    Log.e(TAG, "onSuccess: Failure encrypting location...");
                                    e.printStackTrace();
                                }
                            } else {
                                Log.d(TAG, "onSuccess: Location or user null. Try opening Google Maps and then try pushing the button again.");
                            }
                        }
                    });

        } else {
            Log.d(TAG, "updateUserLocation: Location Permission was denied...");
            requestPermissions();
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_CODE);
            updateUserLocation();
        }
    }

    private void signOut() {
        mAuth.signOut();
        Intent intent_1 = new Intent(this, SignInActivity.class);
        startActivity(intent_1);
        overridePendingTransition(R.anim.enter2, R.anim.exit2);
    }
}
