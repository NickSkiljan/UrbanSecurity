package edu.osu.urban_security.security_receiver_app;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Ravi Tamada on 07/10/16.
 * www.androidhive.info
 */

@IgnoreExtraProperties
public class User {

    public String name;
    public String latitude;
    public String longitude;
    public String altitude;
    public boolean moving;
    public String phone_number;
    public String address;
    public String userId;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String latitude, String longitude, String altitude, boolean moving, String phone_number, String address) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.moving = moving;
        this.phone_number = phone_number;
        this.address = address;
    }
}
