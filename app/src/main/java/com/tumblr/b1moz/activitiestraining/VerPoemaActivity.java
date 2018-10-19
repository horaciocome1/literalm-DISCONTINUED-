package com.tumblr.b1moz.activitiestraining;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tumblr.b1moz.activitiestraining.domain.Poema;
import com.tumblr.b1moz.activitiestraining.helpers.MyDatabaseHelper;

public class VerPoemaActivity extends AppCompatActivity {
    
    TextView textViewTitulo;
    TextView textViewNomeAutor;
    TextView textViewConteudo;
    TextView textViewData;
    
    Button buttonEditar;
    Button buttonEliminar;
    
    Poema poema;
    
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
                intent.putExtra("id", poema.getId());
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
                                (new MyDatabaseHelper(context)).deletePoema(poema.getId());
                                ((VerPoemaActivity) context).finish();
                            }
                        })
                        .setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .show();
            }
        });
    }
    
    @Override
    protected void onPostResume() {
        super.onPostResume();
        poema = (new MyDatabaseHelper(this)).readOne((long) getIntent()
                .getIntExtra("id", - 1));
    
        getSupportActionBar().setSubtitle(poema.getCategoria());
    
        textViewTitulo = findViewById(R.id.textview_titulo_poema2);
        textViewTitulo.setText(poema.getTitulo());
        textViewNomeAutor = findViewById(R.id.textview_nome_autor2);
        textViewNomeAutor.setText(poema.getNomeAutor());
        textViewConteudo = findViewById(R.id.textview_conteudo2);
        textViewConteudo.setText(poema.getConteudo());
        textViewData = findViewById(R.id.textview_data2);
        textViewData.setText(poema.getData());
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
    
}
