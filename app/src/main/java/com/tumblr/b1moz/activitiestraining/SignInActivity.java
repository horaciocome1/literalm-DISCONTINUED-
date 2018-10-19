package com.tumblr.b1moz.activitiestraining;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    
    EditText mEmail;
    EditText mPassword;
    
    Button mSignIn;
    Button mCancel;
    
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    
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
        
        mCancel = findViewById(R.id.activity_sign_in_button_cancel);
    
    }
    
    private void startSignIn() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
    
        if (!(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))) {
            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (! task.isSuccessful())
                                Toast.makeText(SignInActivity.this, "Sign in problem",
                                        Toast.LENGTH_SHORT).show();
                            else
                                finish();
                        }
                    });
        } else
            Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
    }
    
}
