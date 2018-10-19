package com.tumblr.b1moz.activitiestraining;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tumblr.b1moz.activitiestraining.adapters.MyPoemasRecyclerViewAdapter;
import com.tumblr.b1moz.activitiestraining.domain.Poema;
import com.tumblr.b1moz.activitiestraining.helpers.Constants;
import com.tumblr.b1moz.activitiestraining.helpers.MyDatabaseHelper;
import com.wordpress.beendora.simplerecyclerviewtouchlistener.SimpleOnItemTouchListener;
import com.wordpress.beendora.simplerecyclerviewtouchlistener.SimpleRecyclerViewOnItemTouchListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button buttonCadastrar;
    RecyclerView recyclerView;
    
    List<Poema> poemas;
    
    MyDatabaseHelper helper;
    MyPoemasRecyclerViewAdapter mAdapter;
    
    DatabaseReference mDatabaseReference;
    FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser == null)
            signIn();
        
        getSupportActionBar().setTitle("Literal M");

        buttonCadastrar = findViewById(R.id.cadastrar);
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastroActivity.class);

                startActivity(intent);
            }
        });
    
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        helper = new MyDatabaseHelper(this);
        poemas = new ArrayList<>();
        mAdapter = new MyPoemasRecyclerViewAdapter(poemas);
        
        DatabaseReference childRef = mDatabaseReference.child(Constants.RealtimeDatabase.POEMS_NODE);
        childRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Poema poema = dataSnapshot.getValue(Poema.class);
                poema.setId(dataSnapshot.getKey());
                mAdapter.addListItem(poema);
            }
    
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            
            }
    
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        
            }
    
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        
            }
    
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
        
            }
        });
        
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new SimpleRecyclerViewOnItemTouchListener(this,
                recyclerView, new SimpleOnItemTouchListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(MainActivity.this, VerPoemaActivity.class);
                intent.putExtra("id", poemas.get(i).getId());
                startActivity(intent);
            }
    
            @Override
            public void onItemDoubleClick(View view, int i) {
        
            }
    
            @Override
            public void onItemLongPress(View view, int i) {
        
            }
        }));
        
    }
    
    private void signIn() {
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    
        switch (requestCode) {
            case Constants.FirebaseAuthentication.RC_SIGN_IN:
                IdpResponse response = IdpResponse.fromResultIntent(data);
                
                if (resultCode == RESULT_OK)
                    mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                else {
                    Snackbar.make(findViewById(R.id.activity_main_root), "Log in failed",
                            Snackbar.LENGTH_INDEFINITE).setAction("Retry",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    signIn();
                                }
                            })
                            .show();
                }
                break;
                
            case Constants.RequestCode.START_ACCOUNT_ACTIVITY:
                    if (resultCode == RESULT_OK)
                        finish();
                break;
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_account:
                startActivityForResult(new Intent(MainActivity.this, AccountActivity.class),
                        Constants.RequestCode.START_ACCOUNT_ACTIVITY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}
