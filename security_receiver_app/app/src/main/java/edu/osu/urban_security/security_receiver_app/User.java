package edu.osu.urban_security.security_receiver_app;

import com.google.firebase.database.IgnoreExtraProperties;

import java.sql.Timestamp;

@IgnoreExtraProperties
public class User {

    public String name;
    public String latitude;
    public String longitude;
    public String altitude;
    public boolean moving;
    public String phone_number;
    public String address;
    public byte[] privateKey;

    public String userId;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String latitude, String longitude, String altitude, boolean moving, String phone_number, String address, byte[] privateKey) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.moving = moving;
        this.phone_number = phone_number;
        this.address = address;
        this.privateKey = privateKey;
    }
}
