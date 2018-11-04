package com.tumblr.b1moz.literalm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tumblr.b1moz.literalm.helpers.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {
    
    @BindView(R.id.settings_activity_new_version_available_textview) TextView mTextViewNewVersion;
    @BindView(R.id.settings_activity_current_version_textview) TextView mTextViewCurrentVersion;
    ProgressDialog mProgressDialog;
    
    @OnClick(R.id.settings_activity_whatsappgroup_button)
    void onClick(View view) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setAction(Intent.ACTION_VIEW);
        String url = "https://chat.whatsapp.com/HwPkrdcb5w7L8ssCHFpHu2";
        intent.setData(Uri.parse(url));
        intent.setPackage("com.whatsapp");
        startActivity(intent);
    }
    
    DatabaseReference mDatabaseReference;
    private String mAppVersion;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Searching for newer versions ...");
        mProgressDialog.show();
        
        getSupportActionBar().setTitle("Definicoes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            mAppVersion = info.versionName;
            mTextViewCurrentVersion.setText("Current version: " + mAppVersion);
            checkVersion();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void checkVersion() {
        mDatabaseReference.child(Constants.RealtimeDatabase.APP_VERSION_NODE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (mAppVersion.equalsIgnoreCase(dataSnapshot.getValue().toString()))
                            mTextViewNewVersion.setText("No updates available!");
                        else
                            mTextViewNewVersion.setText("New version available: " + dataSnapshot.getValue
                                    ().toString());
                        mProgressDialog.dismiss();
                    }
    
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
        
                    }
                });
    }
    
}
