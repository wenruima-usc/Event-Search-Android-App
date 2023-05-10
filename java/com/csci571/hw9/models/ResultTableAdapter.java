package com.csci571.hw9.models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.csci571.hw9.EventDetailActivity;
import com.csci571.hw9.R;
import com.csci571.hw9.fragments.FavoriteFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ResultTableAdapter extends RecyclerView.Adapter<ResultTableViewHolder> {

    Context context;
    List<ResultTableItem> items;
    private static final int REQUEST_CODE = 1;
    private View view;
    private View rootView;

    public ResultTableAdapter(Context context, List<ResultTableItem> items,View rootView) {
        this.context = context;
        this.items = items;
        this.rootView=rootView;
    }


    @NonNull
    @Override
    public ResultTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view=LayoutInflater.from(context).inflate(R.layout.search_result,parent,false);
        return new ResultTableViewHolder(view);
    }

    public void sharePreference(ResultTableItem resultTableItem) {
        Gson gson = new Gson();
        String itemString = gson.toJson(resultTableItem);
        SharedPreferences sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String keyOrderString = sharedPref.getString("keyOrder", "");
        List<String> keyOrders;
        if (keyOrderString.equals("")) {
            keyOrders = new ArrayList<>();
        } else {
            keyOrders = new ArrayList<>(Arrays.asList(keyOrderString.split(",")));
        }
        if (resultTableItem.isFavorite) {
            editor.putString(resultTableItem.getId(), itemString);
            keyOrders.add(resultTableItem.getId());
            String keyOrderStringNew = TextUtils.join(",", keyOrders);
            editor.putString("keyOrder", keyOrderStringNew);
            Snackbar snackbar=Snackbar.make(rootView,resultTableItem.getEvent()+" added to favorites",Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.snackBar));
            TextView snackBarTextView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            snackBarTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
            snackbar.show();
        } else {
            editor.remove(resultTableItem.getId());
            keyOrders.remove(resultTableItem.getId());
            String keyOrderStringNew = TextUtils.join(",", keyOrders);
            editor.putString("keyOrder", keyOrderStringNew);
            Snackbar snackbar=Snackbar.make(rootView,resultTableItem.getEvent()+" removed from favorites",Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.snackBar));
            TextView snackBarTextView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            snackBarTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
            snackbar.show();

        }
        editor.apply();
    }


    @Override
    public void onBindViewHolder(@NonNull ResultTableViewHolder holder, int position) {
        Picasso picasso = Picasso.get();
        String imgSrc=items.get(position).getImgSrc();
        picasso.load(imgSrc).into(holder.img);
        holder.event.setText(items.get(position).getEvent());
        holder.event.setSelected(true);
        holder.venue.setText(items.get(position).getVenue());
        holder.venue.setSelected(true);
        holder.resultCategory.setText(items.get(position).getCategory());
        holder.date.setText(items.get(position).getDate());
        holder.time.setText(items.get(position).getTime());
        if(items.get(position).isFavorite){
            Drawable drawable = context.getResources().getDrawable(R.drawable.heart_filled);
            holder.isFavorite.setImageDrawable(drawable);
        }
        else{
            Drawable drawable=context.getResources().getDrawable(R.drawable.heart_outline);
            holder.isFavorite.setImageDrawable(drawable);
        }
        holder.isFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.get(holder.getAdapterPosition()).isFavorite=!items.get(holder.getAdapterPosition()).isFavorite;
                if(items.get(holder.getAdapterPosition()).isFavorite){
                    Drawable drawable = context.getResources().getDrawable(R.drawable.heart_filled);
                    holder.isFavorite.setImageDrawable(drawable);
                }
                else{
                    Drawable drawable=context.getResources().getDrawable(R.drawable.heart_outline);
                    holder.isFavorite.setImageDrawable(drawable);
                }
                sharePreference(items.get(holder.getAdapterPosition()));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, EventDetailActivity.class);
                ResultTableItem item=items.get(holder.getAdapterPosition());
                intent.putExtra("item", item);
                ((Activity) v.getContext()).startActivityForResult(intent, REQUEST_CODE);
//                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
