package com.csci571.hw9;

import android.media.metrics.Event;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.csci571.hw9.fragments.ArtistFragment;
import com.csci571.hw9.fragments.DetailFragment;
import com.csci571.hw9.fragments.FavoriteFragment;
import com.csci571.hw9.fragments.SearchFragment;
import com.csci571.hw9.fragments.VenueFragment;
import com.csci571.hw9.models.ArtistDetail;
import com.csci571.hw9.models.EventDetail;
import com.csci571.hw9.models.VenueDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EventDetailPagerAdapter extends FragmentStateAdapter {
    private List<Fragment> fragmentsList=new ArrayList<>();
    private EventDetail eventDetail;
    private List<ArtistDetail> artistDetails;
    private VenueDetail venueDetail;

    public EventDetailPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, EventDetail eventDetail, List<ArtistDetail> artistDetails, VenueDetail venueDetail) {
        super(fragmentManager,lifecycle);
        this.eventDetail=eventDetail;
        this.artistDetails=artistDetails;
        this.venueDetail=venueDetail;

        DetailFragment detailFragment=new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("data", eventDetail); // put your variables into a bundle
        detailFragment.setArguments(args); // set the bundle as the fragment's arguments
        fragmentsList.add(detailFragment);

        ArtistFragment artistFragment=new ArtistFragment();
        Bundle artistArgs=new Bundle();
        artistArgs.putSerializable("data", new ArrayList<>(artistDetails));
        artistFragment.setArguments(artistArgs);
        fragmentsList.add(artistFragment);

        VenueFragment venueFragment= new VenueFragment();
        Bundle venueArgs=new Bundle();
        venueArgs.putSerializable("data",venueDetail);
        venueFragment.setArguments(venueArgs);
        fragmentsList.add(venueFragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentsList.size();
    }
}
