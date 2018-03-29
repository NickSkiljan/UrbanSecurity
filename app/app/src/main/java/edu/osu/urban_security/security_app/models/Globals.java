package edu.osu.urban_security.security_app.models;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;

/**
 * Created by sunnypatel on 3/27/18.
 */

public class Globals {
    private static Globals instance;

    public User user;
    private static final String TAG = "GLOBALS";
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    /**
     * Singleton instance instantiation
     * @return instance of globals.
     */
    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }

    /*
     * TODO: Encryption should happen here
     */
    private String encryptString(String input){
        String output = input;
        return output;
    }
    /**
     * Pushes time stamp to sos
     * Updates user location
     */
    public void pushSOS(){
        Log.d(TAG, "pushing SOS");

        // COMPLETED: push timestamp and user info to firebase
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        mDatabase.child("sos").child(mAuth.getUid()).child("timestamp").setValue(ts.toString());
        mDatabase.child("users").child(mAuth.getUid()).setValue(user);
    }

    /**
     * Restrict the constructor from being instantiated
     */
    private Globals(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }
}
