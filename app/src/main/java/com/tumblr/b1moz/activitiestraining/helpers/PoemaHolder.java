package com.tumblr.b1moz.activitiestraining.helpers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tumblr.b1moz.activitiestraining.R;

public class PoemaHolder extends RecyclerView.ViewHolder {
    
    public TextView titulo, nomeAutor, data;
    
    public PoemaHolder(@NonNull View itemView) {
        super(itemView);
        titulo = itemView.findViewById(R.id.textview_titulo_poema);
        nomeAutor = itemView.findViewById(R.id.textview_nome_autor);
        data = itemView.findViewById(R.id.textview_data);
    }
}