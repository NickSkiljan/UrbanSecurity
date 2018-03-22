package edu.osu.urban_security.security_receiver_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
    private String userId;
    private ArrayList<String> SOSs = new ArrayList<>();
    // [END declare_database_ref]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mSOS = mDatabase.child("users");

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
                                updateList();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }

        };
        t.start();

    }

    public void updateList() {

        mSOS.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    SOSs.add(user.name);
                    System.out.println(user.name);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error: " + databaseError);
            }
        });



        LinearLayout linearLayout = new LinearLayout(this);
        ListView SOSListView = new ListView(this);
        SOSListView.setPadding(20, 300, 20, 100);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, SOSs);
        SOSListView.setAdapter(adapter);


        linearLayout.addView(SOSListView);
        this.setContentView(linearLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }


}
