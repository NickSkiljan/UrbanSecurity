package edu.osu.urban_security.security_app.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by sunnypatel on 3/7/18.
 */

//@IgnoreExtraProperties
public class User {

    public String name;
    public String phoneNumber;

    public String latitude;
    public String longitude;
    public String altitude;

    public boolean moving;
    public String address;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public User(String name, String phoneNumber, String latitude, String longitude, String altitude,
                boolean moving, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.name = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.moving = moving;
        this.address = address;
    }

}
