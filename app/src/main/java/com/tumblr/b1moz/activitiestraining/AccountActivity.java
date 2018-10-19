package com.tumblr.b1moz.activitiestraining;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tumblr.b1moz.activitiestraining.helpers.Constants;

public class AccountActivity extends AppCompatActivity {
    
    Button mLogout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
    
        mLogout = findViewById(R.id.activity_account_logout_button);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(AccountActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent();
                                intent.putExtra(Constants.LOGOUT_EXTRA_NAME, Constants.LOGOUT_EXTRA_DATA);
                                setResult(Constants.RequestCode.START_ACCOUNT_ACTIVITY, intent);
                                
                                finish();
                            }
                        });
            }
        });
    }
    
}
