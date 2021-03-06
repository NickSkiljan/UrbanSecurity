package edu.osu.urban_security.security_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by dsmil on 3/9/2018.
 * Creates the About page
 */

public class OptionsActivity extends AppCompatActivity {

    private Button mSignOutButton;
    private FirebaseAuth mAuth;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        // get saved user's name
        sharedPref= getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String username=sharedPref.getString("username","");

        setContentView(R.layout.activity_options);
        // setup toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(username);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_page, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.main_page:

                Intent intent_1 = new Intent(this,SafetyViewActivity.class);
                startActivity(intent_1);
                overridePendingTransition(R.anim.enter2,R.anim.exit2);
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
