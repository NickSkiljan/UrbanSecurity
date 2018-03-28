package edu.osu.urban_security.security_app.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by sunnypatel on 3/7/18.
 */

@IgnoreExtraProperties
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
        this.latitude = "n/a";
        this.longitude = "n/a";
        this.altitude = "n/a";
        this.address = "n/a";
        this.moving = false;
    }

    public User(String name, String phoneNumber, String latitude, String longitude, String altitude,
                String address, boolean moving) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.address = address;
        this.moving = moving;
    }

}
