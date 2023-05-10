package com.csci571.hw9.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csci571.hw9.R;

public class ResultTableViewHolder extends RecyclerView.ViewHolder {
    ImageView img, isFavorite;
    TextView event, venue,resultCategory,date,time;

    public ResultTableViewHolder(@NonNull View itemView) {
        super(itemView);
        img=itemView.findViewById(R.id.img);
        isFavorite=itemView.findViewById(R.id.is_favorite);
        event=itemView.findViewById(R.id.event);
        venue=itemView.findViewById(R.id.venue);
        resultCategory=itemView.findViewById(R.id.result_category);
        date=itemView.findViewById(R.id.date);
        time=itemView.findViewById(R.id.time);
    }
}
