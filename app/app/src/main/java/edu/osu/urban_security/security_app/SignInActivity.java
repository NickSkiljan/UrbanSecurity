package edu.osu.urban_security.security_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import edu.osu.urban_security.security_app.models.Globals;
import edu.osu.urban_security.security_app.models.User;

/**
 * Created by sunnypatel on 3/7/18.
 */

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignInActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText mNameField;
    private EditText mPhoneField ;
    private Button mSignUpButton;
    Globals g = Globals.getInstance();

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views
        mNameField = findViewById(R.id.field_name);
        mPhoneField = findViewById(R.id.field_phone_number);
        mSignUpButton = findViewById(R.id.button_sign_up);

        // Click listeners
        mSignUpButton.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check auth on Activity start
        if (isSignedIn(mAuth.getCurrentUser())) {
            onAuthSuccess();
        }
    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        final String name = mNameField.getText().toString();
        final String phoneNumber = mPhoneField.getText().toString();

        // Set username in SharedPreferences to user's name specified in mNameField
        sharedPref= getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor=sharedPref.edit();

        String username = mNameField.getText().toString();
        editor.putString("username",username);
        editor.commit();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInAnonymously: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "onComplete: " + user.getUid());
                            // Because name HAS to be a final, this is a way to ensure the name is
                            // updated if the user delete's their account and creates a new one
                            if (!name.equals(mNameField.getText().toString())) {
                                writeNewUser(user.getUid(), mNameField.getText().toString(), mPhoneField.getText().toString());
                            } else {
                                writeNewUser(user.getUid(), name, phoneNumber);
                            }

                            Log.d(TAG, "onComplete: name: " + name);
                            Log.d(TAG, "onComplete: phone#: " + phoneNumber);
                            onAuthSuccess();
                        } else {
                            Log.e(TAG, String.valueOf(task.getException()));
                            Toast.makeText(SignInActivity.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess() {
        // Read value for current user and assign it to g.user (the Global to track user)
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot childSnapshot = dataSnapshot.child("users");
                User userObj = childSnapshot.child(mAuth.getUid()).getValue(User.class);
//                 User(name, phoneNumber, latitude, longitude, altitude, address, moving)
//                g.user = new User(user.name, user.phoneNumber, user.latitude, user.longitude,
//                        user.altitude, user.address, user.moving);
                g.user = userObj;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onAuthSuccess:onCancelled: ", databaseError.toException());
            }
        };
        mDatabase.addListenerForSingleValueEvent(userListener);



        // Go to Main Activity
        startActivity(new Intent(SignInActivity.this, SafetyViewActivity.class));
        overridePendingTransition(R.anim.enter1,R.anim.exit1);
        // finish();
        // startActivity(new Intent(SignInActivity.this, LocationActivity.class));
    }

    private boolean isSignedIn(FirebaseUser user) {
        return user != null;
    }

    private boolean validateForm() {
        boolean result = true;
        String name = mNameField.getText().toString();
        String phone = mPhoneField.getText().toString();
        // Only check if name contains numbers in case user accidently mixes up the name and phone
        // number field.
        if (TextUtils.isEmpty(name)) {
            mNameField.setError("Required");
            result = false;
        }
        else if (name.matches(".*\\d+.*")) {
            mNameField.setError("Name can't contain numbers.");
            result = false;
        }
        else {
            mNameField.setError(null);
        }

        // Ensure phone number contains no dashes or letters (in case user thinks this is the name field
        if (TextUtils.isEmpty(phone)) {
            mPhoneField.setError("Required");
            result = false;
        }
        else if (!TextUtils.isDigitsOnly(phone)) {
            mPhoneField.setError("Numbers only");
            result = false;
        }
        else {
            mPhoneField.setError(null);
        }

        return result;
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String phoneNumber) {
        // Encrypt user's personal information
        AES aes = new AES();
        try {
            // AES key
            SecretKey key = aes.generateKey();
            byte [] encodedKey = key.getEncoded();
            // RSA key to encrypt AES key
//            RSA encryption = new RSA();
            // Encrypted AES key using RSA key
//            byte[] encryptedKey = encryption.encrypt(encodedKey);
            // Push user to Firebase
            key = new SecretKeySpec(AES.AESSecretKeyInBytes, 0, AES.AESSecretKeyInBytes.length, "AES");
            User user = new User(name, phoneNumber, encodedKey, aes, key);
            mDatabase.child("users").child(userId).setValue(user);
            Log.d(TAG, "writeNewUser: Wrote user to Firebase");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // [END basic_write]

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_sign_up) {
            signUp();
        }

    }
}
