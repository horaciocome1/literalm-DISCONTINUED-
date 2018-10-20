package com.tumblr.b1moz.activitiestraining;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tumblr.b1moz.activitiestraining.domain.Poema;
import com.tumblr.b1moz.activitiestraining.helpers.Constants;

public class VerPoemaActivity extends AppCompatActivity {
    
    TextView textViewTitulo;
    TextView textViewNomeAutor;
    TextView textViewConteudo;
    TextView textViewData;
    
    Button buttonEditar;
    Button buttonEliminar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_poema);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Poema");
    
        textViewTitulo = findViewById(R.id.textview_titulo_poema2);
        textViewNomeAutor = findViewById(R.id.textview_nome_autor2);
        textViewConteudo = findViewById(R.id.textview_conteudo2);
        textViewData = findViewById(R.id.textview_data2);
    
        buttonEditar = findViewById(R.id.button_editar);
        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerPoemaActivity.this, CadastroActivity.class);
                intent.putExtra(Constants.POEMA_ID_EXTRA_NAME, getIntent().getStringExtra(Constants.POEMA_ID_EXTRA_NAME));
                startActivity(intent);
            }
        });
    
        final Context context = this;
        
        buttonEliminar = findViewById(R.id.button_eliminar);
        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Pretende eliminar este poema?")
                        .setMessage("Ao eliminar, estes dados serao completamente destruidos. " +
                                "Esta accao e irreversivel.")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removePoema(getIntent().getStringExtra(Constants.POEMA_ID_EXTRA_NAME));
                                finish();
                            }
                        })
                        .setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .show();
            }
        });
        
        bindData();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
    
    private void bindData() {
        FirebaseDatabase.getInstance().getReference().child(Constants.RealtimeDatabase.POEMS_NODE)
                .child(getIntent().getStringExtra(Constants.POEMA_ID_EXTRA_NAME))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        textViewTitulo.setText(dataSnapshot.getValue(Poema.class).getTitulo());
                        textViewNomeAutor.setText(dataSnapshot.getValue(Poema.class).getNomeAutor());
                        textViewData.setText(dataSnapshot.getValue(Poema.class).getData());
                    }
    
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Snackbar.make(findViewById(R.id.activity_ver_poema_root), "Some error detected", Snackbar
                                .LENGTH_INDEFINITE)
                                .setAction("Back", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });
                    }
                });
    
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.RealtimeDatabase.POEMS_DETAILS_NODE)
                .child(getIntent().getStringExtra(Constants.POEMA_ID_EXTRA_NAME))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        textViewConteudo.setText(dataSnapshot.getValue(Poema.class).getConteudo());
                        getSupportActionBar().setSubtitle(dataSnapshot.getValue(Poema.class)
                                .getCategoria());
                    }
                
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Snackbar.make(findViewById(R.id.activity_ver_poema_root), "Some error detected", Snackbar
                                .LENGTH_INDEFINITE)
                                .setAction("Back", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });
                    }
                });
    }
    
    private void removePoema(String key) {
        FirebaseDatabase.getInstance().getReference().child(Constants.RealtimeDatabase.POEMS_NODE)
                .child(getIntent().getStringExtra(Constants.POEMA_ID_EXTRA_NAME)).child(key)
                .removeValue(); // ? setValue(null)
    
        FirebaseDatabase.getInstance().getReference().child(Constants.RealtimeDatabase.POEMS_DETAILS_NODE)
                .child(getIntent().getStringExtra(Constants.POEMA_ID_EXTRA_NAME)).child(key)
                .removeValue(); // ? removeValue()
    }
    
}
