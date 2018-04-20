package edu.osu.urban_security.security_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import edu.osu.urban_security.security_app.models.User;

/**
 * Created by sunnypatel on 3/9/18.
 * Activity with a button that allows us to push the user's information to Firebase for testing.
 */

public class WriteActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "WriteActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private Button mWriteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_write);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views
        mWriteButton = findViewById(R.id.write_button);

        // Click listeners
        mWriteButton.setOnClickListener(this);
    }

    private void updateUserInfo(String userId, String name, String phoneNumber, String latitude,
                                String longitude, String altitude, boolean moving, String address) {
        // Update user at /users/$userId/*
//        User user = new User(name, phoneNumber, latitude, longitude, altitude, moving, address);

        mDatabase.child("users").child(userId).child("name").setValue(name);
        mDatabase.child("users").child(userId).child("phoneNumber").setValue(phoneNumber);
        mDatabase.child("users").child(userId).child("latitude").setValue(longitude);
        mDatabase.child("users").child(userId).child("altitude").setValue(altitude);
        mDatabase.child("users").child(userId).child("moving").setValue(moving);
        mDatabase.child("users").child(userId).child("address").setValue(address);
    }

    private void updateUserInfo(String userId, String latitude, String longitude, String altitude,
                                boolean moving, String address) {
        // Update user at /users/$userId/*
//        User user = new User(name, phoneNumber, latitude, longitude, altitude, moving, address);

        mDatabase.child("users").child(userId).child("latitude").setValue(longitude);
        mDatabase.child("users").child(userId).child("altitude").setValue(altitude);
        mDatabase.child("users").child(userId).child("moving").setValue(moving);
        mDatabase.child("users").child(userId).child("address").setValue(address);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.write_button) {
            updateUserInfo(mAuth.getCurrentUser().getUid(), "test_latitude",
                    "test_longitude", "test_altitude", true,
                    "test_address"
            );
        }
    }
}
