package edu.osu.urban_security.security_receiver_app;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Ravi Tamada on 07/10/16.
 * www.androidhive.info
 */

@IgnoreExtraProperties
public class SOS {

    public String timestamp;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public SOS() {
    }

    public SOS(String timestamp) {
        this.timestamp = timestamp;
    }
}
