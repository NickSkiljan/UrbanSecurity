package edu.osu.urban_security.security_app;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

//    private void signIn() {
//        Log.d(TAG, "signIn");
//        if (!validateForm()) {
//            return;
//        }
//
//        showProgressDialog();
//        String email = mEmailField.getText().toString();
//        String password = mPasswordField.getText().toString();
//
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
//                        hideProgressDialog();
//
//                        if (task.isSuccessful()) {
//                            onAuthSuccess(task.getResult().getUser());
//                        } else {
//                            Toast.makeText(SignInActivity.this, "Sign In Failed",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        final String name = mNameField.getText().toString();
        final String phoneNumber = mPhoneField.getText().toString();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInAnonymously: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            writeNewUser(user.getUid(), name, phoneNumber);
                            onAuthSuccess(user);
                        } else {
                            Toast.makeText(SignInActivity.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
//                        hideProgressDialog();
//
//                        if (task.isSuccessful()) {
//                            onAuthSuccess(task.getResult().getUser());
//                        } else {
//                            Toast.makeText(SignInActivity.this, "Sign Up Failed",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        // Go to MainActivity
        startActivity(new Intent(SignInActivity.this, SafetyViewActivity.class));
        overridePendingTransition(R.anim.enter1,R.anim.exit1);
    }

    private boolean isSignedIn(FirebaseUser user) {
        return user != null;
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mNameField.getText().toString())) {
            mNameField.setError("Required");
            result = false;
        } else {
            mNameField.setError(null);
        }

        if (TextUtils.isEmpty(mPhoneField.getText().toString())) {
            mPhoneField.setError("Required");
            result = false;
        } else {
            mPhoneField.setError(null);
        }

        return result;
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String phoneNumber) {
        User user = new User(name, phoneNumber);

        mDatabase.child("users").child(userId).setValue(user);
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
