package com.tumblr.b1moz.activitiestraining;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

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
    MyDatabaseHelper helper;
    List<Poema> poemas;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
        poemas = helper.readAll();
        recyclerView.setAdapter(new MyPoemasRecyclerViewAdapter(poemas));
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
        poemas = helper.readAll();
        recyclerView.setAdapter(new MyPoemasRecyclerViewAdapter(poemas));
        
    }
    
    private List<Poema> gerarPoemasFalsos(int size) {
        List<Poema> poemas = new ArrayList<>();
        Poema poema;
        for (int i = 0; i < size; i++) {
            poema = new Poema();
            poema.setTitulo("A Leveza do ser");
            poema.setNomeAutor("Genmi Auti");
            poema.setData("13/10/2018");
            poemas.add(poema);
        }
        return poemas;
    }

}
