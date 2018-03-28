package edu.osu.urban_security.security_app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 2;
    public static final int MY_PERMISSIONS_REQUEST_PROCESS_OUTGOING_CALLS = 3;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        final TextView t;
        t = (TextView)findViewById(R.id.textOne);

        t.setText("Checking Permissions");

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_PHONE_STATE},
                MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS},
                MY_PERMISSIONS_REQUEST_PROCESS_OUTGOING_CALLS);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            t.setText("PERMISSION NOT GRANTED");
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                String lat = "Latitude: " + Double.toString(location.getLatitude()) + "\n";
                                String lng = "Longitude: " + Double.toString(location.getLongitude()) + "\n";
                                String alt = "Altitude: " + Double.toString(location.getAltitude()) + "\n";
                                t.setText(lat+lng+alt);
                            }
                        }
                    });
        }

    }
}
