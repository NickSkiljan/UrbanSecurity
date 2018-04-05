package edu.osu.urban_security.security_app.models;


import com.google.firebase.database.IgnoreExtraProperties;


import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import edu.osu.urban_security.security_app.AES;

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

    public String key;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String phoneNumber, byte[] encryptedAESKey, AES aes, SecretKey AESKey) {
        try {

            this.name = new String(aes.encrypt(AESKey, name.getBytes()));
            this.phoneNumber = new String(aes.encrypt(AESKey, phoneNumber.getBytes()));
            this.latitude = "n/a";
            this.longitude = "n/a";
            this.altitude = "n/a";
            this.address = "n/a";
            this.moving = false;

//            this.key = new String(Base64.encode(encryptedAESKey, Base64.NO_WRAP));
            this.key = new String(encryptedAESKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
