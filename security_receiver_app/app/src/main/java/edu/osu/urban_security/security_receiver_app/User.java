package edu.osu.urban_security.security_receiver_app;

import com.google.firebase.database.IgnoreExtraProperties;

import java.sql.Timestamp;

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

        this.decrypt(privateKey);
    }


    public void decrypt(byte[] privateKey) {

        /*AES obj = new AES();

        byte[] decryptedName = obj.decrypt(privateKey, this.name);
        this.name = new String(decryptedName);

        byte[] decryptedLatitude = obj.decrypt(privateKey, this.latitude);
        this.latitude = new String(decryptedLatitude);

        byte[] decryptedLongitude = obj.decrypt(privateKey, this.longitude);
        this.longitude = new String(decryptedLongitude);

        byte[] decryptedAltitude = obj.decrypt(privateKey, this.altitude);
        this.altitude = new String(decryptedAltitude);

        byte[] decryptedPhoneNumber = obj.decrypt(privateKey, this.phone_number);
        this.phone_number = new String(decryptedPhoneNumber);

        byte[] decryptedAddress = obj.decrypt(privateKey, this.address);
        this.address = new String(decryptedAddress);*/

    }
}
