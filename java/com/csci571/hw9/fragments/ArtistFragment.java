package com.csci571.hw9.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csci571.hw9.R;
import com.csci571.hw9.models.ArtistAdapter;
import com.csci571.hw9.models.ArtistDetail;

import java.util.List;


public class ArtistFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_artist, container, false);
        Bundle args = getArguments();
        List<ArtistDetail> artistDetails = (List<ArtistDetail>) args.getSerializable("data");
        RecyclerView recyclerView=view.findViewById(R.id.artist_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ArtistAdapter(getContext(),artistDetails));
        if(artistDetails.size()==0){
            LinearLayout noArtist=view.findViewById(R.id.no_artist_container);
            noArtist.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        return view;
    }
}