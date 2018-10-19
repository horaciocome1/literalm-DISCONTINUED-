package com.tumblr.b1moz.activitiestraining.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tumblr.b1moz.activitiestraining.R;
import com.tumblr.b1moz.activitiestraining.domain.Poema;

import java.util.List;

public class MyPoemasRecyclerViewAdapter extends RecyclerView.Adapter<MyPoemasRecyclerViewAdapter.MyViewHolder> {
    
    List<Poema> mList;
    
    public MyPoemasRecyclerViewAdapter(List<Poema> mList) {
        this.mList = mList;
    }
    
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_poema,
                viewGroup, false);
        return new MyViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.titulo.setText(mList.get(i).getTitulo());
        myViewHolder.nomeAutor.setText(mList.get(i).getNomeAutor());
        myViewHolder.data.setText(mList.get(i).getData());
    }
    
    @Override
    public int getItemCount() {
        return mList.size();
    }
    
    public void addListItem(Poema poema) {
        mList.add(poema);
        notifyItemInserted(getItemCount() - 1);
    }
    
    public void alterListItem(Poema poema) {
        for (int i = 0; i < mList.size(); i++)
            if (mList.get(i).getId().equalsIgnoreCase(poema.getId())) {
                mList.set(i, poema);
                notifyItemChanged(i);
            }
    }
    
    public void removeListItem(String key) {
        for (int i = 0; i < mList.size(); i++)
            if (mList.get(i).getId().equalsIgnoreCase(key)) {
                mList.remove(i);
                notifyItemRemoved(i);
            }
    }
    
    class MyViewHolder extends RecyclerView.ViewHolder {
        
        TextView titulo, nomeAutor, data;
        
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.textview_titulo_poema);
            nomeAutor = itemView.findViewById(R.id.textview_nome_autor);
            data = itemView.findViewById(R.id.textview_data);
        }
        
    }
    
}