package com.csci571.hw9;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.csci571.hw9.fragments.FavoriteFragment;
import com.csci571.hw9.fragments.SearchFragment;
import com.csci571.hw9.models.ResultTableAdapter;
import com.csci571.hw9.models.ResultTableItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity<Spinner> extends AppCompatActivity {
    private  TabLayout tabLayout;
    private  ViewPager2 viewPager2;
    private PagerAdapter pagerAdapter;
    private SharedPreferences sharedPref;
    private int count=0;
    private static final int REQUEST_CODE = 1;

    public void updateSearchFragment(int position){
        SearchFragment searchFragment=(SearchFragment) pagerAdapter.createFragment(position);
        searchFragment.updateContent(sharedPref);
    }


    public void updateFavoriteFragment(int position){
        // Get a reference to the Fragment using the tag
        FavoriteFragment fragment = (FavoriteFragment) pagerAdapter.createFragment(position);

// Call a method to update the contents of the Fragment
        fragment.updateContent(sharedPref);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Update the items list with the modified data
            updateSearchFragment(0);
            updateFavoriteFragment(1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        setTheme(R.style.Theme_Hw9);
        setContentView(R.layout.activity_main);
        this.setTitle("EventFinder");
        Spannable text = new SpannableString(getTitle());
        text.setSpan(new AbsoluteSizeSpan(25, true), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text.setSpan(new ForegroundColorSpan(Color.GREEN), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);

        tabLayout=findViewById(R.id.tab_layout);
        viewPager2=findViewById(R.id.view_pager);
        pagerAdapter=new PagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==1 && count>0){
                    updateFavoriteFragment(tab.getPosition());
                }
                if(tab.getPosition()==0 && count>0){
                    updateSearchFragment(tab.getPosition());
                }
                count++;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

    }
}