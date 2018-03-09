package edu.osu.urban_security.security_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by dsmil on 3/9/2018.
 */

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mSignOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

        mSignOutButton = findViewById(R.id.button_sign_out);

        // Click listeners
        mSignOutButton.setOnClickListener(this);
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

    private void signOut(){

        Intent intent_1 = new Intent(this,SignInActivity.class);
        startActivity(intent_1);
        overridePendingTransition(R.anim.enter2,R.anim.exit2);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_sign_out) {
            signOut();
        }
    }
}
