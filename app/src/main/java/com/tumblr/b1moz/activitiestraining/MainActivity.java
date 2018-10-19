package com.tumblr.b1moz.activitiestraining;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tumblr.b1moz.activitiestraining.adapters.MyPoemasRecyclerViewAdapter;
import com.tumblr.b1moz.activitiestraining.domain.Poema;
import com.tumblr.b1moz.activitiestraining.helpers.MyDatabaseHelper;
import com.wordpress.beendora.simplerecyclerviewtouchlistener.SimpleOnItemTouchListener;
import com.wordpress.beendora.simplerecyclerviewtouchlistener.SimpleRecyclerViewOnItemTouchListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button buttonCadastrar;
    RecyclerView recyclerView;
    
    List<Poema> poemas;
    
    MyDatabaseHelper helper;
    MyPoemasRecyclerViewAdapter mAdapter;
    
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        
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
//        poemas = helper.readAll();
        poemas = new ArrayList<>();
        mAdapter = new MyPoemasRecyclerViewAdapter(poemas);
        
        
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
    
    @Override
    protected void onPostResume() {
        super.onPostResume();
//        poemas = helper.readAll();
//        recyclerView.setAdapter(mAdapter);
        
        DatabaseReference childRef = mDatabaseReference.child("poem-details");
        childRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Poema poema = dataSnapshot.getValue(Poema.class);
                poema.setId(Long.valueOf(dataSnapshot.getKey()));
                mAdapter.addListItem(poema, poemas.size() - 1);
            }
    
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Poema poema = dataSnapshot.getValue(Poema.class);
//                poema.setId(Long.valueOf(dataSnapshot.getKey()));
//                mAdapter.alterListItem(poema);
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
        
    }

}
