package com.csci571.hw9.models;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csci571.hw9.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistResultViewHolder> {
    Context context;
    List<ArtistDetail> artistDetails;

    public ArtistAdapter(Context context, List<ArtistDetail> artistDetails) {
        this.context = context;
        this.artistDetails = artistDetails;
    }

    @NonNull
    @Override
    public ArtistResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArtistResultViewHolder(LayoutInflater.from(context).inflate(R.layout.artist_result,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistResultViewHolder holder, int position) {
        Picasso picasso = Picasso.get();
        String artistImgSrc=artistDetails.get(position).getArtistImg();
        picasso.load(artistImgSrc).into(holder.artistImg);
        holder.artistName.setText(artistDetails.get(position).getName());
        holder.artistName.setSelected(true);
        holder.followers.setText(artistDetails.get(position).getFollowers());
        holder.followers.setSelected(true);
        holder.progressBar.setMax(100);
        holder.progressBar.setProgress(Integer.parseInt(artistDetails.get(position).getPopularity()));
        holder.progressBarValue.setText(artistDetails.get(position).getPopularity());
        String albumFirstSrc=artistDetails.get(position).getAlbums().get(0);
        String albumSecondSrc=artistDetails.get(position).getAlbums().get(1);
        String albumThirdSrc=artistDetails.get(position).getAlbums().get(2);
        picasso.load(albumFirstSrc).into(holder.albumFirst);
        picasso.load(albumSecondSrc).into(holder.albumSecond);
        picasso.load(albumThirdSrc).into(holder.albumThird);
        holder.popularity.setText("Popularity");
        holder.popularAlbums.setText("Popular Albums");
        String text="Check out on Spotify";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        holder.spotifyUrl.setText(spannableString);
        holder.spotifyUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(artistDetails.get(holder.getAdapterPosition()).getSpotifyUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                v.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return artistDetails.size();
    }
}
