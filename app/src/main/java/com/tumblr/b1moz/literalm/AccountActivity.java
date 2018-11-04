package com.tumblr.b1moz.literalm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tumblr.b1moz.literalm.helpers.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class AccountActivity extends AppCompatActivity {
    
    
    @BindView(R.id.activity_account_cover_image) ImageView mCoverImage;
    @BindView(R.id.activity_account_profile_image) ImageView mProfileImage;
    @BindView(R.id.activity_account_display_name_textview) TextView mDisplayName;
    @BindView(R.id.activity_account_display_email_textview) TextView mEmail;
    
    @OnClick(R.id.activity_account_logout_button)
    public void onClick(View view) {
        AuthUI.getInstance()
                .signOut(AccountActivity.this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent();
                        intent.putExtra(Constants.LOGOUT_EXTRA_NAME, Constants.LOGOUT_EXTRA_DATA);
                        setResult(RESULT_OK, intent);
                    
                        finish();
                    }
                });
    }
    
    FirebaseUser mFirebaseUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        bindUserData();
        
        getSupportActionBar().setTitle("Meu perfil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    
    private void bindUserData() {
        Glide.with(this)
                .load(mFirebaseUser.getPhotoUrl())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,3)))
                .into(mCoverImage);
        Glide.with(this)
                .load(mFirebaseUser.getPhotoUrl())
                .into(mProfileImage);
        mDisplayName.setText(mFirebaseUser.getDisplayName());
        mEmail.setText(mFirebaseUser.getEmail());
    }
    
}
