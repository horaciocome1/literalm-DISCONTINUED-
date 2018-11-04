package com.tumblr.b1moz.literalm.helpers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tumblr.b1moz.literalm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostHolder extends RecyclerView.ViewHolder {
    
    @BindView(R.id.item_post_image) public ImageView cover;
    @BindView(R.id.item_post_title) public TextView title;
    @BindView(R.id.item_post_author) public TextView author;
    @BindView(R.id.item_post_date) public TextView date;
    
    public PostHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
    
}
