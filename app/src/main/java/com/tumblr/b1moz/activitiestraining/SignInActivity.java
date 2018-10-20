package com.tumblr.b1moz.activitiestraining;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tumblr.b1moz.activitiestraining.helpers.Constants;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {
    
    EditText mEmail;
    EditText mPassword;
    
    Button mSignIn;
    Button mProviders;
    
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        
        getSupportActionBar().hide();
    
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (!(firebaseAuth.getCurrentUser() == null))
                    finish();
            }
        };
        
        mEmail = findViewById(R.id.activity_sign_in_email);
        mPassword = findViewById(R.id.activity_sign_in_password);
        
        mSignIn = findViewById(R.id.activity_sign_in_button_sign_in);
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });
        
        mProviders = findViewById(R.id.activity_sign_in_button_providers);
        mProviders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useProviders();
            }
        });
    
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    
        switch (requestCode) {
            case Constants.FirebaseAuthentication.RC_SIGN_IN:
                IdpResponse response = IdpResponse.fromResultIntent(data);
    
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK);
                    finish();
                }
                else {
                    Toast.makeText(this, "Some error occured. Please try again!",
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    
    
    private void startSignIn() {
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();
    
        if (!(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))) {
            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignInActivity.this,
                                        "Your credentials are not registred.\nRegistring now ...",
                                        Toast.LENGTH_LONG).show();
                                createAccount(email, password);
                            }
                            else
                                finish();
                        }
                    });
        } else
            Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
    }
    
    private void useProviders() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
                Constants.FirebaseAuthentication.RC_SIGN_IN);
    }
    
    private void createAccount(String email, String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            setResult(RESULT_OK);
                            finish();
                        } else
                            Toast.makeText(SignInActivity.this, "Authentication failed. Please " +
                                            "try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
}
