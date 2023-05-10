package com.csci571.hw9;

import static android.media.CamcorderProfile.get;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.csci571.hw9.fragments.FavoriteFragment;
import com.csci571.hw9.fragments.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentStateAdapter {
    private List<Fragment> fragmentsList=new ArrayList<>();


    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        SearchFragment searchFragment=new SearchFragment();
        FavoriteFragment favoriteFragment=new FavoriteFragment();

        fragmentsList.add(searchFragment);
        fragmentsList.add(favoriteFragment);
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
