package com.csci571.hw9.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csci571.hw9.R;
import com.csci571.hw9.models.EventDetail;
import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment {
    private EventDetail eventDetail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        if (args != null) {
            eventDetail = (EventDetail) args.getSerializable("data");
        }
        View view= inflater.inflate(R.layout.fragment_detail, container, false);
        if(eventDetail.getArtists().equals("")){
            LinearLayout layout=view.findViewById(R.id.artist_teams_layout);
            layout.setVisibility(View.GONE);
        }
        else{
            TextView textView=view.findViewById(R.id.artist_teams_value);
            textView.setText(eventDetail.getArtists());
            textView.setSelected(true);
        }
        if(eventDetail.getVenue().equals("")){
            LinearLayout layout=view.findViewById(R.id.venue_layout);
            layout.setVisibility(View.GONE);
        }
        else{
            TextView textView=view.findViewById(R.id.venue_value);
            textView.setText(eventDetail.getVenue());
            textView.setSelected(true);
        }
        if(eventDetail.getDate().equals("")){
            LinearLayout layout=view.findViewById(R.id.date_layout);
            layout.setVisibility(View.GONE);
        }
        else{
            TextView textView=view.findViewById(R.id.date_value);
            textView.setText(eventDetail.getDate());
            textView.setSelected(true);
        }
        if(eventDetail.getTime().equals("")){
            LinearLayout layout=view.findViewById(R.id.time_layout);
            layout.setVisibility(View.GONE);
        }
        else{
            TextView textView=view.findViewById(R.id.time_value);
            textView.setText(eventDetail.getTime());
            textView.setSelected(true);
        }
        if(eventDetail.getGenres().equals("")){
            LinearLayout layout=view.findViewById(R.id.genres_layout);
            layout.setVisibility(View.GONE);
        }
        else{
            TextView textView=view.findViewById(R.id.genres_value);
            textView.setText(eventDetail.getGenres());
            textView.setSelected(true);
        }
        if(eventDetail.getPriceRanges().equals(" (USD)")){
            LinearLayout layout=view.findViewById(R.id.price_range_layout);
            layout.setVisibility(View.GONE);
        }
        else{
            TextView textView=view.findViewById(R.id.price_range_value);
            textView.setText(eventDetail.getPriceRanges());
            textView.setSelected(true);
        }
        if(eventDetail.getStatus().equals("")){
            LinearLayout layout=view.findViewById(R.id.ticket_status_layout);
            layout.setVisibility(View.GONE);
        }
        else{
            TextView textView=view.findViewById(R.id.ticket_status_value);
            textView.setText(eventDetail.getStatus());
            ColorStateList colorStateList;
            switch (eventDetail.getStatus()){
                case "On Sale":
                    colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.green));
                    textView.setBackgroundTintList(colorStateList);
                    break;
                case "Off Sale":
                    colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.red));
                    textView.setBackgroundTintList(colorStateList);
                    break;
                case "Canceled":
                    colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.black));
                    textView.setBackgroundTintList(colorStateList);
                    break;
                case "Postponed":
                    colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.orange));
                    textView.setBackgroundTintList(colorStateList);
                    break;
                case "Rescheduled":
                    colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.orange));
                    textView.setBackgroundTintList(colorStateList);
                    break;
                default:
                    colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.red));
                    textView.setBackgroundTintList(colorStateList);
                    break;
            }
        }
        if(eventDetail.getTicketUrl().equals("")){
            LinearLayout layout=view.findViewById(R.id.buy_layout);
            layout.setVisibility(View.GONE);
        }
        else{
            TextView textView=view.findViewById(R.id.buy_value);
            String text=eventDetail.getTicketUrl();
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
            textView.setText(spannableString);
            textView.setSelected(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url=eventDetail.getTicketUrl();
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
        if(eventDetail.getSeatmapUrl().equals("")){
            ImageView imageView=view.findViewById(R.id.seatmap);
            imageView.setVisibility(View.GONE);
        }
        else{
            ImageView imageView=view.findViewById(R.id.seatmap);
            Picasso picasso = Picasso.get();
            String imgSrc=eventDetail.getSeatmapUrl();
            picasso.load(imgSrc).into(imageView);
        }
        return view;
    }
}