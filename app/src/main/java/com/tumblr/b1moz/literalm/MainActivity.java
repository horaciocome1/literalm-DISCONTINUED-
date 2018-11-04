package com.tumblr.b1moz.literalm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tumblr.b1moz.literalm.domain.Post;
import com.tumblr.b1moz.literalm.helpers.Constants;
import com.tumblr.b1moz.literalm.helpers.PostHolder;
import com.wordpress.beendora.simplerecyclerviewtouchlistener.SimpleOnItemTouchListener;
import com.wordpress.beendora.simplerecyclerviewtouchlistener.SimpleRecyclerViewOnItemTouchListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    
    @BindView(R.id.activity_main_recyclerview) RecyclerView recyclerView;
    ProgressDialog mProgressDialog;
    
    @OnClick(R.id.activity_main_post_button)
    void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(intent);
    }
    
    FirebaseRecyclerAdapter mAdapter;
    
    DatabaseReference mDatabaseReference;
    FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading posts ...");
        if (!isUserLogged())
            signIn();
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        setupList();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        mProgressDialog.show();
        mAdapter.startListening();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.RequestCode.START_SIGN_IN_ACTIVITY:
                if (resultCode == RESULT_OK)
                    mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                else
                    finish();
                break;
            case Constants.RequestCode.START_ACCOUNT_ACTIVITY:
                if (resultCode == RESULT_OK)
                    signIn();
                break;
            default:
                finish();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isUserLogged())
            switch (item.getItemId()) {
                case R.id.main_menu_account:
                    startActivityForResult(new Intent(MainActivity.this, AccountActivity.class),
                            Constants.RequestCode.START_ACCOUNT_ACTIVITY);
                    return true;
                case R.id.main_menu_settings:
                    startActivity(new Intent(this, SettingsActivity.class));
                    return true;
                case R.id.main_menu_help:
                    Intent intent = new Intent("android.intent.action.MAIN");
                    intent.setAction(Intent.ACTION_VIEW);
                    String url = "https://chat.whatsapp.com/HwPkrdcb5w7L8ssCHFpHu2";
                    intent.setData(Uri.parse(url));
                    intent.setPackage("com.whatsapp");
                    startActivity(intent);
                    return true;
            }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
    
    private void signIn() {
        startActivityForResult(new Intent(MainActivity.this, SignInActivity.class), Constants
                .RequestCode.START_SIGN_IN_ACTIVITY);
    }
    
    private void setupList() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.RealtimeDatabase.POSTS_NODE);
        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(mDatabaseReference, new SnapshotParser<Post>() {
                    Post post;
                    @NonNull
                    @Override
                    public Post parseSnapshot(@NonNull DataSnapshot snapshot) {
                        post = snapshot.getValue(Post.class);
                        post.setKey(snapshot.getKey());
                        return post;
                    }
                }).build();
        mAdapter = new FirebaseRecyclerAdapter<Post, PostHolder>(options) {
    
            @NonNull
            @Override
            public PostHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                        .item_post, viewGroup, false);
                return new PostHolder(view);
            }
    
            @Override
            protected void onBindViewHolder(@NonNull PostHolder holder, int position,
                    @NonNull Post model) {
                holder.title.setText(model.getTitle());
                holder.author.setText(model.getAuthor());
                holder.date.setText(model.getDate());
                Glide.with(MainActivity.this)
                        .load(model.getImageUrl())
                        .into(holder.cover);
                mProgressDialog.dismiss();
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new SimpleRecyclerViewOnItemTouchListener(this,
                recyclerView, new SimpleOnItemTouchListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                intent.putExtra(Constants.POST_KEY_EXTRA_NAME, ((Post) mAdapter.getItem(i)).getKey());
                startActivity(intent);
            }
    
            @Override
            public void onItemDoubleClick(View view, int i) {}
    
            @Override
            public void onItemLongPress(View view, int i) {}
        }));
    }
    
    private boolean isUserLogged() {
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser == null) {
            return false;
        }
        return true;
    }
    
}
