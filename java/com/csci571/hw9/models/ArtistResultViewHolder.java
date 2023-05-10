package com.csci571.hw9.models;


import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csci571.hw9.R;

public class ArtistResultViewHolder extends RecyclerView.ViewHolder {
    ImageView artistImg,albumFirst,albumSecond,albumThird;
    TextView artistName, followers, progressBarValue,spotifyUrl,popularity,popularAlbums;
    ProgressBar progressBar;
    public ArtistResultViewHolder(@NonNull View itemView) {
        super(itemView);
        artistImg=itemView.findViewById(R.id.artist_img);
        albumFirst=itemView.findViewById(R.id.album_first);
        albumSecond=itemView.findViewById(R.id.album_second);
        albumThird=itemView.findViewById(R.id.album_third);
        artistName=itemView.findViewById(R.id.artist_name);
        followers=itemView.findViewById(R.id.followers);
        spotifyUrl=itemView.findViewById(R.id.spotify_link);
        progressBarValue=itemView.findViewById(R.id.artist_progress_value);
        progressBar=itemView.findViewById(R.id.artist_progress_bar);
        popularity=itemView.findViewById(R.id.popularity);
        popularAlbums=itemView.findViewById(R.id.popular_albums);

    }
}
