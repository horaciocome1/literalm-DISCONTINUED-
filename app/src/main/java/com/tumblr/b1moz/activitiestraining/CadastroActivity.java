package com.tumblr.b1moz.activitiestraining;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tumblr.b1moz.activitiestraining.domain.Poema;
import com.tumblr.b1moz.activitiestraining.helpers.MyDatabaseHelper;

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
        
        final Context context = this;
        
        final int id = getIntent().getIntExtra("id", -1);
        if (!(id == -1)) {
            getSupportActionBar().setSubtitle("Editar poema");
            
            Poema poema = (new MyDatabaseHelper(this)).readOne((long) id);
            editTextTitulo.setText(poema.getTitulo());
            editTextNomeAutor.setText(poema.getNomeAutor());
            editTextConteudo.setText(poema.getConteudo());
            editTextCategoria.setText(poema.getCategoria());
            editTextData.setText(poema.getData());
        } else
            getSupportActionBar().setSubtitle("Novo poema");

        buttonCancelar = findViewById(R.id.button_cancelar_cadastro);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    
        
        buttonCadastrar = findViewById(R.id.button_cadastrar);
        if (id == -1)
            buttonCadastrar.setText("Cadastrar");
        else
            buttonCadastrar.setText("Editar");
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Poema poema = new Poema();
                poema.setTitulo(editTextTitulo.getText().toString());
                poema.setNomeAutor(editTextNomeAutor.getText().toString());
                poema.setData(editTextData.getText().toString());
                
                MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(context);
                if (id == -1) {
                    myDatabaseHelper.addPoema(poema);
                    
                    DatabaseReference myRef = mFirebaseDatabase.getReference();
                    
                    DatabaseReference childRef = myRef.child("poem-details");
                    childRef.push().setValue(poema);
    
                    poema.setCategoria(editTextCategoria.getText().toString());
                    poema.setConteudo(editTextConteudo.getText().toString());
                    childRef = myRef.child("poems");
                    childRef.push().setValue(poema);
                }
                else {
                    poema.setId((long) id);
                    myDatabaseHelper.updatePoema(poema);
                }
                
                finish();
            }
        });
    }
    
}
