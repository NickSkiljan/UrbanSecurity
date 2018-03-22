package edu.osu.urban_security.security_receiver_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private DatabaseReference mSOS;
    // [END declare_database_ref]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mSOS = mDatabase.child("users");

        LinearLayout linearLayout = new LinearLayout(this);
        ListView SOSListView = new ListView(this);
        SOSListView.setPadding(20, 300, 20, 100);

        ArrayList<String> SOSs = new ArrayList<>();

        SOSs.add("Nick Skiljan");
        SOSs.add("Cloudy Patel");
        SOSs.add("Joe Simons");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, SOSs);
        SOSListView.setAdapter(adapter);


        linearLayout.addView(SOSListView);
        this.setContentView(linearLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));




    }
}
