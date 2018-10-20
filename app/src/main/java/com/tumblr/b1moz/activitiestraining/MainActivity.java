package com.tumblr.b1moz.activitiestraining;

import android.content.Intent;
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
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tumblr.b1moz.activitiestraining.domain.Poema;
import com.tumblr.b1moz.activitiestraining.helpers.Constants;
import com.tumblr.b1moz.activitiestraining.helpers.PoemaHolder;
import com.wordpress.beendora.simplerecyclerviewtouchlistener.SimpleOnItemTouchListener;
import com.wordpress.beendora.simplerecyclerviewtouchlistener.SimpleRecyclerViewOnItemTouchListener;

public class MainActivity extends AppCompatActivity {

    Button buttonCadastrar;
    RecyclerView recyclerView;
    
    FirebaseRecyclerAdapter mAdapter;
    
    DatabaseReference mDatabaseReference;
    FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        if (!isUserLogged())
            signIn();
        
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        buttonCadastrar = findViewById(R.id.cadastrar);
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });
        
        setupList();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
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
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constants
                .RealtimeDatabase.POEMS_NODE);
        
        FirebaseRecyclerOptions<Poema> options = new FirebaseRecyclerOptions.Builder<Poema>()
                .setQuery(mDatabaseReference, new SnapshotParser<Poema>() {
                    @NonNull
                    @Override
                    public Poema parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Poema poema = snapshot.getValue(Poema.class);
                        poema.setId(snapshot.getKey());
                        return poema;
                    }
                }).build();
    
        mAdapter = new FirebaseRecyclerAdapter<Poema, PoemaHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PoemaHolder holder, int position,
                    @NonNull Poema model) {
                holder.titulo.setText(model.getTitulo());
                holder.nomeAutor.setText(model.getNomeAutor());
                holder.data.setText(model.getData());
            }
        
            @NonNull
            @Override
            public PoemaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                        .item_poema, viewGroup, false);
                return new PoemaHolder(view);
            }
        };
        
        recyclerView.setAdapter(mAdapter);
        
        recyclerView.addOnItemTouchListener(new SimpleRecyclerViewOnItemTouchListener(this,
                recyclerView, new SimpleOnItemTouchListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(MainActivity.this, VerPoemaActivity.class);
                intent.putExtra(Constants.POEMA_ID_EXTRA_NAME, ((Poema) mAdapter.getItem(i)).getId());
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
    
    private boolean isUserLogged() {
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser == null) {
            return false;
        }
        return true;
    }
    
}
