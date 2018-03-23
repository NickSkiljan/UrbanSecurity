package edu.osu.urban_security.security_receiver_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // [START declare_database_ref]
    private static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseReference mDatabase;
    private DatabaseReference mSOS;
    private DatabaseReference mUsers;
    private ArrayList<User> SOSs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //SOSs.clear();

        mSOS.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    DatabaseReference userId = mUsers.child(snapshot.getKey());
                    userId.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            System.out.println(snapshot.getKey());
                            User user = snapshot.getValue(User.class);

                            if (user != null) {
                                user.userId = snapshot.getKey().toString();

                                int index = -1;
                                for(int x = 0; x < SOSs.size(); x++) {
                                    System.out.println("#1: " + SOSs.get(x).userId);
                                    System.out.println("#2: " + user.userId);

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

        //return SOSs;



//        LinearLayout linearLayout = new LinearLayout(this);
//        ListView SOSListView = new ListView(this);
//        SOSListView.setPadding(20, 300, 20, 100);

        UsersAdapter adapter = new UsersAdapter(this, SOSs);
        ListView listView = findViewById(R.id.lvUsers);
        listView.setAdapter(adapter);
    }

}
