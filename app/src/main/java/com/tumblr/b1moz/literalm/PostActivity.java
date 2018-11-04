package com.tumblr.b1moz.literalm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tumblr.b1moz.literalm.domain.Post;
import com.tumblr.b1moz.literalm.helpers.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostActivity extends AppCompatActivity {

    @BindView(R.id.activity_post_title_edittext) EditText editTextTitle;
    @BindView(R.id.activity_post_content_edittext) EditText editTextContent;
    @BindView(R.id.activity_post_image_button) ImageButton mButtonImage;
    private ProgressDialog mProgressDialog;
    
    @OnClick({R.id.activity_post_publish_button, R.id.activity_post_cancel_button, R.id.activity_post_image_button})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_post_publish_button:
                uploadCoverImage();
                break;
            case R.id.activity_post_image_button:
                pickImage();
                return;
        }
    }
    
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    private FirebaseUser mFirebaseUser;
    
    private String mKey = null;
    private Uri mCoverImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
    
        mProgressDialog = new ProgressDialog(this);
        
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        
        getSupportActionBar().setTitle("Publicar");
    
        mKey = mDatabaseReference.child(Constants.RealtimeDatabase.POSTS_NODE).push().getKey();
        if (!isBrandNewPost())
            bindPostData();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.RequestCode.PICK_IMAGE_FROM_GALLERY:
                if (resultCode == RESULT_OK) {
                    mCoverImageUri = data.getData();
                    Glide.with(PostActivity.this)
                            .load(mCoverImageUri)
                            .into(mButtonImage);
                }
                break;
        }
    }
    
    private boolean isBrandNewPost(){
        if (getIntent().getStringExtra(Constants.POST_KEY_EXTRA_NAME) == null)
            return true;
        mKey = getIntent().getStringExtra(Constants.POST_KEY_EXTRA_NAME);
        return false;
    }
    
    private void bindPostData() {
        mProgressDialog.show();
        mDatabaseReference.child(Constants.RealtimeDatabase.POSTS_NODE)
                .child(mKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Post post = dataSnapshot.getValue(Post.class);
                        editTextTitle.setText(post.getTitle());
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
                        editTextContent.setText(dataSnapshot.getValue().toString());
                    }
    
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
        
                    }
                });
        mDatabaseReference.child(Constants.RealtimeDatabase.POSTS_NODE)
                .child(mKey)
                .child(Constants.RealtimeDatabase.POST_DETAILS_NODE_IMAGEURL_ATRIBUTE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Glide.with(PostActivity.this)
                                .load(dataSnapshot.getValue().toString())
                                .into(mButtonImage);
                        mProgressDialog.dismiss();
                    }
    
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
        
                    }
                });
    }
    
    private void edit(final Uri imageUrl) {
        mDatabaseReference
                .child(Constants.RealtimeDatabase.POSTS_NODE)
                .child(mKey)
                .child(Constants.RealtimeDatabase.POST_NODE_TITLE_ATRIBUTE)
                .setValue(editTextTitle.getText().toString());
        mDatabaseReference
                .child(Constants.RealtimeDatabase.POST_CONTENT_NODE)
                .child(mKey)
                .setValue(editTextContent.getText().toString());
        if (!(imageUrl == null))
            mDatabaseReference
                    .child(Constants.RealtimeDatabase.POSTS_NODE)
                    .child(mKey)
                    .child(Constants.RealtimeDatabase.POST_DETAILS_NODE_IMAGEURL_ATRIBUTE)
                    .setValue(imageUrl.toString());
        mDatabaseReference
                .child(Constants.RealtimeDatabase.USER_POSTS_NODE)
                .child(mFirebaseUser.getUid())
                .child(mKey)
                .child(Constants.RealtimeDatabase.POST_NODE_TITLE_ATRIBUTE)
                .setValue(editTextTitle.getText().toString());
        finish();
    }
    
    private void post(final Uri imageUrl) {
        Post post = new Post(mFirebaseUser.getDisplayName(), editTextTitle.getText().toString(),
                imageUrl.toString());
        mDatabaseReference
                .child(Constants.RealtimeDatabase.POSTS_NODE)
                .child(mKey)
                .setValue(post);
        mDatabaseReference
                .child(Constants.RealtimeDatabase.USER_POSTS_NODE)
                .child(mFirebaseUser.getUid())
                .child(mKey)
                .setValue(post);
        mDatabaseReference
                .child(Constants.RealtimeDatabase.POST_CONTENT_NODE)
                .child(mKey)
                .setValue(editTextContent.getText().toString());
        finish();
    }
    
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.RequestCode.PICK_IMAGE_FROM_GALLERY);
    }
    
    private void uploadCoverImage() {
        if (!(mCoverImageUri == null)) {
            mProgressDialog.setMessage("Uploading cover photo ...");
            mProgressDialog.show();
            final StorageReference reference = mStorageReference
                    .child(Constants.FirebaseStorage.POST_COVERS_NODE)
                    .child(mKey).child(mCoverImageUri.getLastPathSegment());
            UploadTask uploadTask = reference.putFile(mCoverImageUri);
            Task<Uri> uriTask = uploadTask.continueWithTask(
                    new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
                                throws Exception {
                            if (! task.isSuccessful())
                                throw task.getException();
                            return reference.getDownloadUrl();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            mProgressDialog.setMessage("Finishing ...");
                            if (isBrandNewPost())
                                post(task.getResult());
                            else
                                edit(task.getResult());
                        }
                    });
        } else if (!isBrandNewPost())
            edit(null);
    }
    
}
