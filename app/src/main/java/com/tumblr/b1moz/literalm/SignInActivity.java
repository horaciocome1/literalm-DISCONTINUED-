package com.tumblr.b1moz.literalm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tumblr.b1moz.literalm.domain.User;
import com.tumblr.b1moz.literalm.helpers.Constants;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {
    
    @BindView(R.id.activity_sign_in_email_edittext) EditText edittextEmail;
    @BindView(R.id.activity_sign_in_password_edittext) EditText edittextPassword;
    
    @OnClick({R.id.activity_sign_in_button, R.id.activity_sign_in_button_providers})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_sign_in_button:
                startSignIn();
                break;
            case R.id.activity_sign_in_button_providers:
                useProviders();
                break;
        }
    }
    
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseUser mFirebaseUser;
    
    ProgressDialog mProgressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
    
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Preparing your own enviroment ...");
        
        getSupportActionBar().hide();
    
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (!(firebaseAuth.getCurrentUser() == null))
                    finish();
            }
        };
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    
        switch (requestCode) {
            case Constants.FirebaseAuthentication.RC_SIGN_IN:
                IdpResponse response = IdpResponse.fromResultIntent(data);
    
                if (resultCode == RESULT_OK) {
                    newUser();
                    setResult(RESULT_OK);
                    finish();
                }
                else
                    Toast.makeText(this, "Some error occured. Please try again!",
                            Toast.LENGTH_LONG).show();
                break;
        }
    }
    
    private void startSignIn() {
        final String email = edittextEmail.getText().toString();
        final String password = edittextPassword.getText().toString();
    
        if (!(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))) {
            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignInActivity.this,
                                        "Your credentials are not registred.",
                                        Toast.LENGTH_LONG).show();
//                                createAccount(email, password);
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
    
    private void newUser() {
        mProgressDialog.show();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.RealtimeDatabase.USERS_NODE)
                .child(mFirebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(User.class) == null)
                            writeUserData();
                        else
                            finish();
                    }
    
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
        
                    }
                });
    }
    
    private void writeUserData() {
        mProgressDialog.setMessage("Aquiring your data ...");
        User user = new User(mFirebaseUser.getDisplayName(), mFirebaseUser.getEmail());
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.RealtimeDatabase.USERS_NODE)
                .child(mFirebaseUser.getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
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
