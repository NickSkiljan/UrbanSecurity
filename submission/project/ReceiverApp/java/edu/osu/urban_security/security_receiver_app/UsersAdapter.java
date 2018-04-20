package edu.osu.urban_security.security_receiver_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
/**
 * Authors: Nick, Sunny, Maxwell
 * Adapter for our recycler view that allows us to populate the recycler view with decrypted
 *   sos-user's personal information
 */
public class UsersAdapter extends ArrayAdapter<User> {



    public UsersAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvLatitude = (TextView) convertView.findViewById(R.id.tvLatitude);
        TextView tvLongitude = (TextView) convertView.findViewById(R.id.tvLongitude);
        TextView tvAltitude = (TextView) convertView.findViewById(R.id.tvAltitude);
        AES aes = new AES();
        String username = "default";
        String latitude = "default";
        String longitude = "default";
        String altitude = "default";
        try {
            SecretKey key = new SecretKeySpec(AES.AESSecretKeyInBytes, 0, AES.AESSecretKeyInBytes.length, "AES");
            username = AES.decryptString(key, user.name);
            latitude = AES.decryptString(key, user.latitude);
            longitude = AES.decryptString(key, user.longitude);
            altitude = AES.decryptString(key, user.altitude);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Populate the data into the template view using the data object
        tvName.setText(username);
        tvLatitude.setText("Lat: " + latitude);
        tvLongitude.setText("Long: " + longitude);
        tvAltitude.setText("Alt: " + altitude);
        // Return the completed view to render on screen
        return convertView;
    }
}