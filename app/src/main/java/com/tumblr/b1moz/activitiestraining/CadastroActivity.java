package com.tumblr.b1moz.activitiestraining;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tumblr.b1moz.activitiestraining.domain.Poema;
import com.tumblr.b1moz.activitiestraining.helpers.Constants;

public class CadastroActivity extends AppCompatActivity {

    Button buttonCancelar;
    Button buttonCadastrar;
    EditText editTextTitulo;
    EditText editTextNomeAutor;
    EditText editTextConteudo;
    EditText editTextCategoria;
    EditText editTextData;
    
    FirebaseDatabase mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
    
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        
        getSupportActionBar().setTitle("Cadastro");
    
        editTextTitulo = findViewById(R.id.edittext_titulo);
        editTextNomeAutor = findViewById(R.id.edittext_nome_autor);
        editTextConteudo = findViewById(R.id.edittext_conteudo);
        editTextCategoria = findViewById(R.id.edittext_categoria);
        editTextData = findViewById(R.id.edittext_data);
    
        buttonCadastrar = findViewById(R.id.button_cadastrar);
    
        buttonCancelar = findViewById(R.id.button_cancelar_cadastro);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        if (getIntent().getStringExtra(Constants.POEMA_ID_EXTRA_NAME) == null)
            cadastrar();
        else
            editar();
    }
    
    private void editar() {
        getSupportActionBar().setSubtitle("Editar poema");
        bindData();
        
        buttonCadastrar.setText("Editar");
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Poema poema = new Poema();
                poema.setTitulo(editTextTitulo.getText().toString());
                poema.setNomeAutor(editTextNomeAutor.getText().toString());
                poema.setData(editTextData.getText().toString());
                mFirebaseDatabase.getReference().child(Constants.RealtimeDatabase.POEMS_NODE)
                        .child(getIntent().getStringExtra(Constants.POEMA_ID_EXTRA_NAME)).setValue(poema);
    
                poema = new Poema();
                poema.setCategoria(editTextCategoria.getText().toString());
                poema.setConteudo(editTextConteudo.getText().toString());
                mFirebaseDatabase.getReference().child(Constants.RealtimeDatabase.POEMS_DETAILS_NODE)
                        .child(getIntent().getStringExtra(Constants.POEMA_ID_EXTRA_NAME)).setValue(poema);
                
                finish();
            }
        });
    }
    
    private void cadastrar() {
        getSupportActionBar().setSubtitle("Novo poema");
        
        buttonCadastrar.setText("Cadastrar");
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Poema poema = new Poema();
                poema.setTitulo(editTextTitulo.getText().toString());
                poema.setNomeAutor(editTextNomeAutor.getText().toString());
                poema.setData(editTextData.getText().toString());
                String key = mFirebaseDatabase.getReference().child(Constants.RealtimeDatabase.POEMS_NODE).push()
                        .getKey();
                mFirebaseDatabase.getReference().child(Constants.RealtimeDatabase.POEMS_NODE).child(key).setValue(poema);
    
                poema = new Poema();
                poema.setCategoria(editTextCategoria.getText().toString());
                poema.setConteudo(editTextConteudo.getText().toString());
                mFirebaseDatabase.getReference().child(Constants.RealtimeDatabase.POEMS_DETAILS_NODE).child(key)
                        .setValue(poema);
                
                finish();
            }
        });
    }
    
    private void bindData() {
        FirebaseDatabase.getInstance().getReference().child(Constants.RealtimeDatabase.POEMS_NODE)
                .child(getIntent().getStringExtra(Constants.POEMA_ID_EXTRA_NAME))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        editTextTitulo.setText(dataSnapshot.getValue(Poema.class).getTitulo());
                        editTextNomeAutor.setText(dataSnapshot.getValue(Poema.class).getNomeAutor());
                        editTextData.setText(dataSnapshot.getValue(Poema.class).getData());
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
                        editTextConteudo.setText(dataSnapshot.getValue(Poema.class).getConteudo());
                        editTextCategoria.setText(dataSnapshot.getValue(Poema.class)
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
    
}
