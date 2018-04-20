package edu.osu.urban_security.security_receiver_app;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Nick Skiljan
 * Model for an SOS, which is essentially a timestamp tied to a user.
 * This allows us to easily retrieve the SOS information from Firebase.x
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
