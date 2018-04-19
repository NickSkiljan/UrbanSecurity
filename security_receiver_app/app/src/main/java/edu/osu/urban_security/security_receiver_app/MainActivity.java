package edu.osu.urban_security.security_receiver_app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //Declare database references
    private DatabaseReference mDatabase;
    private DatabaseReference mSOS;
    private DatabaseReference mUsers;

    //Create empty list of users
    private ArrayList<User> SOSs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get database references
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mSOS = mDatabase.child("sos");
        mUsers = mDatabase.child("users");

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {

                        //Update every second
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                update();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }

        };
        t.start();

    }

    public void update() {
        // Sorted by most recent
        mSOS.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    DatabaseReference userId = mUsers.child(snapshot.getKey());
                    // Listener that ensures data is up to date
                    userId.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            User user = snapshot.getValue(User.class);

                            if (user != null) {
                                user.userId = snapshot.getKey().toString();

                                int index = -1;
                                for(int x = 0; x < SOSs.size(); x++) {
                                    if (SOSs.get(x).userId.equals(user.userId)) {
                                        index = x;
                                        x = SOSs.size();
                                    }
                                }

                                if (index  < 0) {
                                    SOSs.add(0, user);
                                } else {
                                    SOSs.remove(index);
                                    SOSs.add(0, user);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                                System.out.println("Error: " + databaseError);
                            }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error: " + databaseError);
            }
        });


        // Set up Recycler view
        UsersAdapter adapter = new UsersAdapter(this, SOSs);
        final ListView listView = findViewById(R.id.lvUsers);
        // If receiver clicks on a user, receiver should get directions to user
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Extract user data
                User user = (User) listView.getItemAtPosition(i);
                String name = user.name;
                String encryptedLatitude = user.latitude;
                String encryptedLongitude = user.longitude;
                String latitude = "";
                String longitude = "";

                //Decrypt latitude and longitude
                AES aes = new AES();
                try {
                    SecretKey key = new SecretKeySpec(AES.AESSecretKeyInBytes, 0, AES.AESSecretKeyInBytes.length, "AES");
                    latitude = AES.decryptString(key, encryptedLatitude);
                    longitude = AES.decryptString(key, encryptedLongitude);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Log click
                Log.d(TAG, "Clicked on SOS");
                String logString = "Selected SOS for " + name;
                Log.d(TAG, logString);

                //Open google maps
                String uriString = "google.navigation:q=" + latitude + "," + longitude;
                Uri gmmIntentUri = Uri.parse(uriString);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });
        listView.setAdapter(adapter);
    }

    //Convert database string to timestamp
    public Timestamp createTimestamp(String ts) {

        Timestamp timestamp;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
            Date parsedTimeStamp = dateFormat.parse(ts);
            timestamp = new Timestamp(parsedTimeStamp.getTime());


        } catch (Exception e){
            System.out.println("Parse error: " + e);
            timestamp = new Timestamp(new java.util.Date().getTime());
        }

        return timestamp;
    }

}
