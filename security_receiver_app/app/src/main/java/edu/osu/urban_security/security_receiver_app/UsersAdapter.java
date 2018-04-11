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

public class UsersAdapter extends ArrayAdapter<User> {
    public byte[] AESSecretKeyInBytes = {-34, 86, -34, 74, 119, 119, -64, -46, 86, 93, 55, -117, 52, -19, 4, 94};

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
        String username = "test";
        try {
            SecretKey key = new SecretKeySpec(AESSecretKeyInBytes, 0, AESSecretKeyInBytes.length, "AES");
            username = AES.decryptString(key, user.name);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Populate the data into the template view using the data object
        tvName.setText(username);
        tvLatitude.setText("Lat: " + user.latitude);
        tvLongitude.setText("Long: " + user.longitude);
        tvAltitude.setText("Alt: " + user.altitude);
        // Return the completed view to render on screen
        return convertView;
    }
}