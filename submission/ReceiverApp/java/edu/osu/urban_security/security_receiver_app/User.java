package edu.osu.urban_security.security_receiver_app;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

import java.sql.Timestamp;

import javax.crypto.SecretKey;

import static android.content.ContentValues.TAG;
/**
 * Authors: Nick, Sunny
 * Model for User.
 */
@IgnoreExtraProperties
public class User {

    public String name;
    public String latitude;
    public String longitude;
    public String altitude;
    public String phone_number;
    public String address;
    public byte[] privateKey;

    public String userId;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String latitude, String longitude, String altitude, String phone_number, String address, byte[] privateKey) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.phone_number = phone_number;
        this.address = address;
        this.privateKey = privateKey;

        //this.decrypt(privateKey);
    }

    public void decrypt(SecretKey privateKey, AES obj) throws Exception {


        String decryptedName = obj.decryptString(privateKey, this.name);
        this.name = decryptedName;
        Log.d(TAG, "decrypt: " + decryptedName);


        String decryptedLatitude = obj.decryptString(privateKey, this.latitude);
        this.latitude = new String(decryptedLatitude);

        String decryptedLongitude = obj.decryptString(privateKey, this.longitude);
        this.longitude = new String(decryptedLongitude);

        String decryptedAltitude = obj.decryptString(privateKey, this.altitude);
        this.altitude = new String(decryptedAltitude);

        String decryptedPhoneNumber = obj.decryptString(privateKey, this.phone_number);
        this.phone_number = new String(decryptedPhoneNumber);

        String decryptedAddress = obj.decryptString(privateKey, this.address);
        this.address = new String(decryptedAddress);

    }
}
