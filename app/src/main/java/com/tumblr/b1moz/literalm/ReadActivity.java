package com.tumblr.b1moz.literalm;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tumblr.b1moz.literalm.domain.Post;
import com.tumblr.b1moz.literalm.helpers.Constants;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReadActivity extends AppCompatActivity {
    
    @BindView(R.id.activity_read_title_textview) TextView mTitle;
    @BindView(R.id.activity_read_author_textview) TextView mAuthor;
    @BindView(R.id.activity_read_content_textview) TextView mContent;
    @BindView(R.id.activity_read_date_textview) TextView mDate;
    @BindView(R.id.activity_read_cover_image) ImageView mCover;
    private ProgressDialog mProgressDialog;
    
    @OnClick({R.id.activity_read_delete_button, R.id.activity_read_user_account_button, R.id
            .activity_read_rating_button, R.id.activity_read_edit_button, R.id.activity_read_share_button})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_read_edit_button:
                Intent intent = new Intent(ReadActivity.this, PostActivity.class);
                intent.putExtra(Constants.POST_KEY_EXTRA_NAME, mKey);
                startActivity(intent);
                break;
            case R.id.activity_read_rating_button:
                rate();
                break;
            case R.id.activity_read_share_button:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getTextToShare());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Send to ..."));
                break;
        }
    }
    
    private String mKey;
    @BindArray(R.array.rating_list) String[] rating_list;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mFirebaseUser;
    private int rating = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        ButterKnife.bind(this);
    
        mProgressDialog = new ProgressDialog(this);
    
        mKey = getIntent().getStringExtra(Constants.POST_KEY_EXTRA_NAME);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Post");
        
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    
        bindPostData();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
    
    private void bindPostData() {
        mProgressDialog.setMessage("Setting all up ...");
        mProgressDialog.show();
        mDatabaseReference.child(Constants.RealtimeDatabase.POSTS_NODE)
                .child(mKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!(dataSnapshot.getValue() == null)) {
                            Post post = dataSnapshot.getValue(Post.class);
                            mTitle.setText(post.getTitle());
                            mAuthor.setText(post.getAuthor());
                            mDate.setText(post.getDate());
                            Glide.with(ReadActivity.this)
                                    .load(post.getImageUrl())
                                    .into(mCover);
                        }
                    }
    
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
        
                    }
                });
        mDatabaseReference.child(Constants.RealtimeDatabase.POST_CONTENT_NODE)
                .child(mKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!(dataSnapshot.getValue() == null))
                            mContent.setText(dataSnapshot.getValue().toString());
                        else
                            finish();
                        mProgressDialog.dismiss();
                    }
    
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
        
                    }
                });
    }
    
    private void rate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rate!")
                .setSingleChoiceItems(rating_list, rating, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // pull to realtime database
                mProgressDialog.setMessage("Loading ...");
                mProgressDialog.show();
    
//                mDatabaseReference.child(Constants.RealtimeDatabase.POST_USERS_RATING_NODE)
//                        .child(mFirebaseUser.getUid())
//                        .setValue(which);
                Toast.makeText(ReadActivity.this, "This funtion is not fully available now",
                        Toast.LENGTH_SHORT).show();
                
                dialog.dismiss();
            }
        })
                .create()
                .show();
    }
    
    private String getTextToShare() {
        return mTitle.getText().toString() + "\n\n" + mContent.getText().toString();
    }
    
}
