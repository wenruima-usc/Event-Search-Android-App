package com.csci571.hw9.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.csci571.hw9.models.FavoriteAdapter;
import com.csci571.hw9.models.ResultTableItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FavoriteFragment extends Fragment {
    private View view;
    private List<ResultTableItem> items;
    private RecyclerView recyclerView;
    private LinearLayout noFavoritesContainer;
    private FavoriteAdapter favoriteAdapter;


    public void updateContent(SharedPreferences sharedPref){
        Gson gson = new Gson();
        items=new ArrayList<>();

// loop through the key-value pairs and log them
        String keyOrderString = sharedPref.getString("keyOrder", "");
        if(!keyOrderString.equals("")){
            List<String> keyOrders=new ArrayList<>(Arrays.asList(keyOrderString.split(",")));
            for(int i=keyOrders.size()-1;i>=0;i--){
                String key=keyOrders.get(i);
                String itemString=sharedPref.getString(key,null);
                ResultTableItem item=gson.fromJson(itemString,ResultTableItem.class);
                items.add(item);
            }
            noFavoritesContainer.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            FavoriteAdapter.OnItemRemovedListener listener = new FavoriteAdapter.OnItemRemovedListener() {
                @Override
                public void onItemRemoved() {
                    // Update something else when an item is removed
                    if(favoriteAdapter.getItemCount()==0){
                        noFavoritesContainer.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                }
            };

            favoriteAdapter=new FavoriteAdapter(getContext(),items,listener,view);
            recyclerView.setAdapter(favoriteAdapter);
        }
        else{
            noFavoritesContainer.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView=view.findViewById(R.id.favorite_recycler_view);
        noFavoritesContainer=view.findViewById(R.id.no_favorite_container);
        SharedPreferences sharedPref= getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        updateContent(sharedPref);
        return  view;
    }

}