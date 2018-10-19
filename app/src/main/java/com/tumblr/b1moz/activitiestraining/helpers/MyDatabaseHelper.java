package com.tumblr.b1moz.activitiestraining.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tumblr.b1moz.activitiestraining.domain.Poema;

import java.util.ArrayList;
import java.util.List;

import static com.tumblr.b1moz.activitiestraining.helpers.Constants.Database.TablePoemas.col0;
import static com.tumblr.b1moz.activitiestraining.helpers.Constants.Database.TablePoemas.col1;
import static com.tumblr.b1moz.activitiestraining.helpers.Constants.Database.TablePoemas.col2;
import static com.tumblr.b1moz.activitiestraining.helpers.Constants.Database.TablePoemas.col3;
import static com.tumblr.b1moz.activitiestraining.helpers.Constants.Database.TablePoemas.col4;
import static com.tumblr.b1moz.activitiestraining.helpers.Constants.Database.TablePoemas.col5;
import static com.tumblr.b1moz.activitiestraining.helpers.Constants.Database.TablePoemas
        .createTable;
import static com.tumblr.b1moz.activitiestraining.helpers.Constants.Database.TablePoemas.dropTable;
import static com.tumblr.b1moz.activitiestraining.helpers.Constants.Database.TablePoemas
        .whereIdClause;
import static com.tumblr.b1moz.activitiestraining.helpers.Constants.Database.name;
import static com.tumblr.b1moz.activitiestraining.helpers.Constants.Database.version;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    
    public MyDatabaseHelper(Context context) {
        super(context, name, null, version);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTable);
        onCreate(db);
    }
    
    public void addPoema(Poema poema) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(col1, poema.getTitulo());
        values.put(col2, poema.getNomeAutor());
        values.put(col3, poema.getConteudo());
        values.put(col4, poema.getCategoria());
        values.put(col5, poema.getData());
        database.insert(Constants.Database.TablePoemas.name, null, values);
    }
    
    public void updatePoema(Poema poema) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(col1, poema.getTitulo());
        values.put(col2, poema.getNomeAutor());
        values.put(col3, poema.getConteudo());
        values.put(col4, poema.getCategoria());
        values.put(col5, poema.getData());
        database.update(Constants.Database.TablePoemas.name, values, whereIdClause, new
                String[]{String.valueOf(poema.getId())});
    }
    
    public List<Poema> readAll() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(Constants.Database.TablePoemas.name, new String[]{col0,
                        col1, col2, col5}, null, null,
               null, null, null);
        List<Poema> poemas = new ArrayList<>();
        Poema poema;
        while (cursor.moveToNext()) {
            poema = new Poema();
            poema.setId(cursor.getString(0));
            poema.setTitulo(cursor.getString(1));
            poema.setNomeAutor(cursor.getString(2));
            poema.setData(cursor.getString(3));
            poemas.add(poema);
        }
        return poemas;
    }
    
    public Poema readOne(String id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(Constants.Database.TablePoemas.name, new String[]{col0,
                        col1, col2, col3, col4, col5}, whereIdClause, new String[]{String.valueOf
                        (id)}, null, null, null);
        Poema poema = new Poema();
        if (cursor.moveToFirst()) {
            poema.setId(cursor.getString(0));
            poema.setTitulo(cursor.getString(1));
            poema.setNomeAutor(cursor.getString(2));
            poema.setConteudo(cursor.getString(3));
            poema.setCategoria(cursor.getString(4));
            poema.setData(cursor.getString(5));
        }
        return poema;
    }
    
    public void deletePoema(String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(Constants.Database.TablePoemas.name, whereIdClause, new String[]{String
                .valueOf(id)});
    }
    
}
