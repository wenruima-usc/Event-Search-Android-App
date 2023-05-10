package com.csci571.hw9;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.csci571.hw9.models.ArtistDetail;
import com.csci571.hw9.models.EventDetail;
import com.csci571.hw9.models.ResultTableAdapter;
import com.csci571.hw9.models.ResultTableItem;
import com.csci571.hw9.models.VenueDetail;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class EventDetailActivity extends AppCompatActivity {
    private EventDetail eventDetail;
    private List<ArtistDetail> artistDetails=new ArrayList<>();
    private LinearLayout progressBar;
    private ViewPager2 eventDetailViewPager;
    private VenueDetail venueDetail;
    private boolean artistFinish=false;
    private boolean venueFinish=false;
    private String name;
    private String address;
    private String phoneNum;
    private String openHours;
    private String generalRule;
    private String childRule;
    private String latitude;
    private String longitude;
    private ResultTableItem item;
    private ImageView favorite;
    private ContentFrameLayout rootView;


    public String formatDate(String inputDateString) throws ParseException {
        if(inputDateString.equals("")){
            return inputDateString;
        }
        String format;
        if (inputDateString.contains(" ")) {
            format = "yyyy-MM-dd HH:mm:ss";
        } else {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(format);
        Date date = inputDateFormat.parse(inputDateString);

        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String outputDateString = outputDateFormat.format(date);
        return outputDateString;
    }

    public String formatTime(String inputDateString) throws ParseException {
        if(inputDateString.equals("")){
            return inputDateString;
        }
        if(!inputDateString.contains(" ")){
            return "";
        }
        String  format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(format);
        Date date = inputDateFormat.parse(inputDateString);

        SimpleDateFormat outputDateFormat = new SimpleDateFormat("h:mm a");
        String outputTimeString = outputDateFormat.format(date);
        return outputTimeString;
    }

    public List<String> getArtists(String artist){
        String[] namesArray = artist.split("\\|");

        List<String> namesList = new ArrayList<>();
        for (String name : namesArray) {
            namesList.add(name.trim());
        }
        Set<String> uniqueNames = new HashSet<>(namesList);
        List<String> outputNames = new ArrayList<>(uniqueNames);
        return outputNames;
    }

    public String formatFollowers(String followersString){
        long followers = Long.parseLong(followersString.replaceAll(",", ""));
        if (followers >= 1000000000) {
            return (followers / 1000000000) + "B Followers";
        } else if (followers >= 1000000) {
            return (followers / 1000000) + "M Followers";
        } else if (followers >= 1000) {
            return (followers / 1000) + "K Followers";
        } else {
            return followers + " Followers";
        }
    }

    public void getArtistDetail(List<String> artists) throws InterruptedException {
        RequestQueue queue = Volley.newRequestQueue(this);
        List<CompletableFuture<JSONObject>> futures = new ArrayList<>();

        for (String artist : artists) {
            CompletableFuture<JSONObject> future = new CompletableFuture<>();
            String url = "https://csci571hw8-4869.wl.r.appspot.com/searchArtist/" + artist;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            future.complete(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    future.completeExceptionally(error);
                }
//
            });
            queue.add(jsonObjectRequest);
            futures.add(future);
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));

        allFutures.thenAcceptAsync(new Consumer<Void>() {
            @Override
            public void accept(Void aVoid) {
                // All the CompletableFutures have completed, so we can now combine the responses
                 artistDetails = new ArrayList<>();
                for (CompletableFuture<JSONObject> future : futures) {
                    try {
                        JSONObject response = future.get();
                        JSONObject jsonObject = response.getJSONObject("data");
                        String name = jsonObject.getString("name");
                        String artistImg = jsonObject.getString("artistImg");
                        String popularity = jsonObject.getString("popularity");
                        String followers = jsonObject.getString("followers");
                        String followersFromatted=formatFollowers(followers);
                        String spotifyUrl = jsonObject.getString("spotifyUrl");
                        List<String> albums = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("albums");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String album = jsonArray.getString(i);
                            albums.add(album);
                        }
                        ArtistDetail artistDetail = new ArtistDetail(name, artistImg, popularity, followersFromatted,
                                spotifyUrl, albums);
                        artistDetails.add(artistDetail);
                        if(artistDetails.size()==artists.size()){
                            artistFinish=true;
                            if(artistFinish && venueFinish){
                                EventDetailPagerAdapter eventDetailPagerAdapter = new EventDetailPagerAdapter(getSupportFragmentManager(),getLifecycle(),eventDetail,artistDetails,venueDetail);
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    eventDetailViewPager.setAdapter(eventDetailPagerAdapter);
                                    progressBar.setVisibility(View.GONE);
                                    eventDetailViewPager.setVisibility(View.VISIBLE);
                                });
                            }
                        }

                    } catch (Exception e) {
                        // Handle exceptions here
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    public void getGeoLngLat(String address){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+address+"&key=AIzaSyBon3YcFzZlcOEqA_1VX8gq9i5Iy7lb-io";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response here
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            latitude = Double.toString(jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                            longitude = Double.toString(jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                            // Use the latitude and longitude values here
                            venueDetail=new VenueDetail(name,address,phoneNum,openHours,generalRule,childRule,longitude,latitude);
                            venueFinish=true;
                            if(venueFinish && artistFinish){
                                EventDetailPagerAdapter eventDetailPagerAdapter = new EventDetailPagerAdapter(getSupportFragmentManager(),getLifecycle(),eventDetail,artistDetails,venueDetail);
                                eventDetailViewPager.setAdapter(eventDetailPagerAdapter);
                                progressBar.setVisibility(View.GONE);
                                eventDetailViewPager.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            // Handle the JSON exception here
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error here
                        error.printStackTrace();
                    }
                });
        queue.add(request);
    }
    public void getVenueDetail(String venueName){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

// Set the URL for the request.
        String url = "https://csci571hw8-4869.wl.r.appspot.com/venueDetail/"+venueName;

// Create a JSONObjectRequest object.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Process the response.
                        try {
                            JSONObject jsonObject=response.getJSONObject("data");
                            name=jsonObject.getString("name");
                            address=jsonObject.getString("address");
                            phoneNum=jsonObject.getString("phoneNum");
                            openHours=jsonObject.getString("openHours");
                            generalRule=jsonObject.getString("generalRule");
                            childRule=jsonObject.getString("childRule");
                            getGeoLngLat(address);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error.
                        error.printStackTrace();
                    }
                });

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public static boolean isMusicRelated(String genre) {
        if (genre.equals("")) {
            return false;
        }
        String[] genres = genre.replace(" ", "").split("\\|");
        if (genres[0].toLowerCase().equals("music")) {
            return true;
        }
        return false;
    }
    public void sharePreference(){
        Gson gson = new Gson();
        String itemString = gson.toJson(item);
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String keyOrderString = sharedPref.getString("keyOrder", "");
        List<String> keyOrders;
        if(keyOrderString.equals("")){
            keyOrders=new ArrayList<>();
        }
        else{
            keyOrders=new ArrayList<>(Arrays.asList(keyOrderString.split(",")));
        }
        if(item.isFavorite()){
            editor.putString(item.getId(), itemString);
            keyOrders.add(item.getId());
            String keyOrderStringNew= TextUtils.join(",", keyOrders);
            editor.putString("keyOrder",keyOrderStringNew);
            Snackbar snackbar=Snackbar.make(rootView,item.getEvent()+" added to favorites",Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.snackBar));
            TextView snackBarTextView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            snackBarTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
            snackbar.show();


        }
        else{
            editor.remove(item.getId());
            keyOrders.remove(item.getId());
            String keyOrderStringNew= TextUtils.join(",", keyOrders);
            editor.putString("keyOrder",keyOrderStringNew);
            Snackbar snackbar=Snackbar.make(rootView,item.getEvent()+" removed from favorites",Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.snackBar));
            TextView snackBarTextView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            snackBarTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
            snackbar.show();

        }
        editor.apply();
    }

    public void setFavoriteIcon(){
        if(item.isFavorite()){
            Drawable drawable = getResources().getDrawable(R.drawable.heart_filled);
            favorite.setImageDrawable(drawable);
        }
        else{
            Drawable drawable = getResources().getDrawable(R.drawable.heart_outline);
            favorite.setImageDrawable(drawable);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hw9);
        item = (ResultTableItem) getIntent().getSerializableExtra("item");
        rootView=findViewById(android.R.id.content);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.event_detail_action_bar, null);
        ImageView twitter=customView.findViewById(R.id.twitter);
        ImageView facebook=customView.findViewById(R.id.facebook);
        favorite=customView.findViewById(R.id.detail_favorite);
        setFavoriteIcon();
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setFavorite(!item.isFavorite());
                setFavoriteIcon();
                sharePreference();
            }
        });
        TextView title=customView.findViewById(R.id.action_bar_title);
        title.setText(item.getEvent());
        getSupportActionBar().setCustomView(customView);
        TextView actionBarTitle=customView.findViewById(R.id.action_bar_title);
        actionBarTitle.setSelected(true);
        setContentView(R.layout.activity_event_detail);
        AppCompatButton backButton=customView.findViewById(R.id.action_bar_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        TabLayout tabLayout = findViewById(R.id.event_detail_tab_layout);
        TabLayout.Tab detailTab =  tabLayout.getTabAt(0);
        TabLayout.Tab artistTab =  tabLayout.getTabAt(1);
        TabLayout.Tab venueTab =  tabLayout.getTabAt(2);


        View customTabLayoutView1 = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null);
        ImageView detailImg = customTabLayoutView1.findViewById(R.id.tab_img);
        TextView detailText=customTabLayoutView1.findViewById(R.id.tab_text);
        detailText.setText("DETAILS");
        detailImg.setImageResource(R.drawable.info_icon);
        detailTab.setCustomView(customTabLayoutView1);

        View customTabLayoutView2 = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null);
        ImageView artistImg = customTabLayoutView2.findViewById(R.id.tab_img);
        TextView artistText= customTabLayoutView2.findViewById(R.id.tab_text);
        artistText.setText("ARTIST(S)");
        int drawableResId = R.drawable.artist_icon;

// Define the desired width and height
        int desiredWidth = 60;
        int desiredHeight = 60;

// Decode the image from the drawable resource and create a scaled bitmap
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), drawableResId);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, false);
        artistImg.setImageBitmap(scaledBitmap);
        artistImg.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        artistText.setTextColor(ContextCompat.getColor(this, R.color.white));
        artistTab.setCustomView(customTabLayoutView2);


        View customTabLayoutView3 = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null);
        ImageView venueImg = customTabLayoutView3.findViewById(R.id.tab_img);
        TextView venueText= customTabLayoutView3.findViewById(R.id.tab_text);
        venueText.setText("VENUE");
        venueImg.setImageResource(R.drawable.venue_icon);
        venueImg.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        venueText.setTextColor(ContextCompat.getColor(this, R.color.white));
        venueTab.setCustomView(customTabLayoutView3);

        eventDetailViewPager=findViewById(R.id.event_detail_view_pager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                ImageView tabIcon = customView.findViewById(R.id.tab_img);
                TextView tabText = customView.findViewById(R.id.tab_text);
                tabIcon.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.green), PorterDuff.Mode.SRC_IN);
                tabText.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.green));
                eventDetailViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                ImageView tabIcon = customView.findViewById(R.id.tab_img);
                TextView tabText = customView.findViewById(R.id.tab_text);
                tabIcon.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.white), PorterDuff.Mode.SRC_IN);
                tabText.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        eventDetailViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        progressBar=findViewById(R.id.detail_progress_bar_container);
        progressBar.setVisibility(View.VISIBLE);
        eventDetailViewPager.setVisibility(View.GONE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url="https://csci571hw8-4869.wl.r.appspot.com/eventDetail/"+item.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        try {
                            JSONObject jsonObject=response.getJSONObject("data");
                            String id=jsonObject.getString("id");
                            String name=jsonObject.getString("name");
                            String date=jsonObject.getString("date");
                            String artist=jsonObject.getString("artist");
                            String venue=jsonObject.getString("venue");
                            String genre=jsonObject.getString("genre");
                            String priceRange=jsonObject.getString("priceRange")+" (USD)";
                            String status=jsonObject.getString("status");
                            String ticketUrl=jsonObject.getString("buyTicketAt");
                            String seatmapUrl=jsonObject.getString("seatmap");
                            String formatDate=formatDate(date);
                            String formatTime=formatTime(date);
                            eventDetail=new EventDetail(id,name,formatDate,formatTime,artist,venue,genre
                            ,priceRange,status,ticketUrl,seatmapUrl);
                            if(!artist.equals("") && isMusicRelated(genre)){
                                List<String> artists=getArtists(artist);
                                getArtistDetail(artists);
                            }
                            else{
                                artistFinish=true;
                            }
                            getVenueDetail(venue);
                            twitter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String tweet = "Check out " +eventDetail.getName()+" on TicketMaster!\n"+eventDetail.getTicketUrl();
                                    String tweetUrl = "https://twitter.com/intent/tweet?text=" + Uri.encode(tweet);
                                    Uri uri = Uri.parse(tweetUrl);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                            });
                            facebook.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = "https://www.facebook.com/sharer/sharer.php?u="+eventDetail.getTicketUrl();
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(intent);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                    }
                });

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.onBackPressed();
        finish();
    }
}