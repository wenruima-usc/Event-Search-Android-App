package com.csci571.hw9.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.csci571.hw9.MainActivity;
import com.csci571.hw9.PagerAdapter;
import com.csci571.hw9.R;
import com.csci571.hw9.models.ResultTableAdapter;
import com.csci571.hw9.models.ResultTableItem;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.Result;

public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private boolean userChangedText=true;
    private Handler handler = new Handler();

    List<ResultTableItem> items=new ArrayList<>();
    RecyclerView resultTableView;
    private View view;


    public void updateContent(SharedPreferences sharedPref){
            for(ResultTableItem curItem: items){
                String id=curItem.getId();
                if(sharedPref.getString(id,null)==null){
                    curItem.setFavorite(false);
                }
                else{
                    curItem.setFavorite(true);
                }
            }
            resultTableView.setAdapter(new ResultTableAdapter(getContext(),items,view));
    }

    public boolean validation(AutoCompleteTextView keywordEditText, EditText distanceEditText, EditText locationEditText, SwitchCompat autoDetect){
        String keyword=keywordEditText.getText().toString();
        String distance=distanceEditText.getText().toString();
        boolean isChecked=autoDetect.isChecked();
        if((!keyword.isEmpty()&&!distance.isEmpty()&&isChecked) || !keyword.isEmpty()&&!distance.isEmpty()&&!locationEditText.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }

    public String formatDate(String inputDateString) throws ParseException {
        if(inputDateString.equals("")){
            return inputDateString;
        }
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date inputDate = inputDateFormat.parse(inputDateString);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String outputDateString = outputDateFormat.format(inputDate);
        return outputDateString;
    }

    public String formatTime(String timeString) throws ParseException {
        if(timeString.equals("")){
            return timeString;
        }
        DateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("h:mm a");
        Date date = inputFormat.parse(timeString);
        String formattedTime = outputFormat.format(date);
        return formattedTime;
    }


    public void showSearchResult(RecyclerView resultTableView,LinearLayout progressBarContainer,LinearLayout noeventContainer,String keyword,String distance, String category, String longitude, String latitude) {
        progressBarContainer.setVisibility(View.VISIBLE);
        items=new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        if(category.equals("All")){
            category="default";
        }
        String url="https://csci571hw8-4869.wl.r.appspot.com/search/"+keyword+"/"+category+"/"+distance+"/"+latitude+"/"+longitude;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                String id=dataObject.getString("id");
                                String event=dataObject.getString("event");
                                String venue=dataObject.getString("venue");
                                String category=dataObject.getString("genre");
                                String date=dataObject.getString("date");
                                String time=dataObject.getString("time");
                                String imgSrc=dataObject.getString("icon");
                                String formatDate=formatDate(date);
                                String formatTime=formatTime(time);
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                Gson gson = new Gson();
                                ResultTableItem item;
                                if(sharedPreferences.getString(id,null)==null){
                                    item=new ResultTableItem(id,imgSrc,event,venue,category,formatDate,formatTime,false);
                                }
                                else{
                                    String itemString=sharedPreferences.getString(id,null);
                                    item = gson.fromJson(itemString, ResultTableItem.class);
                                }
                                items.add(item);
                                // Parse individual dataObject here
                            }
                            progressBarContainer.setVisibility(View.GONE);
                            if(items.size()==0){
                                noeventContainer.setVisibility(View.VISIBLE);
                            }
                            else{
                                resultTableView.setLayoutManager(new LinearLayoutManager(getContext()));
                                resultTableView.setAdapter(new ResultTableAdapter(getContext(),items,view));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_search, container, false);
        spinner= view.findViewById(R.id.spinner);
        resultTableView=view.findViewById(R.id.search_result_recycler_view);
        LinearLayout noEventContainer=view.findViewById(R.id.no_event_container);
        LinearLayout resultProgressBarContainer=view.findViewById(R.id.result_progress_bar_container);
        CardView cardView=view.findViewById(R.id.cardView);
        RelativeLayout result=view.findViewById(R.id.result);
        result.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> categoryAdapter=ArrayAdapter.createFromResource(getContext(),
                R.array.categories,
                R.layout.spinner_selected
                );
        categoryAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(categoryAdapter);
        spinner.setOnItemSelectedListener(this);
        AutoCompleteTextView keywordEditText= view.findViewById(R.id.keyword);
        EditText distanceEditText =  view.findViewById(R.id.distance);
        EditText locationEditText= view.findViewById(R.id.location);
        Spinner spinner= view.findViewById(R.id.spinner);
        SwitchCompat autoDetect = view.findViewById(R.id.autoDetect);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        autoDetect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    locationEditText.setText("");
                    locationEditText.setEnabled(false);
                    locationEditText.setVisibility(View.GONE);
                }
                else{
                    locationEditText.setEnabled(true);
                    locationEditText.setVisibility(View.VISIBLE);
                }
            }
        });

        AppCompatButton submit=view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                if(validation(keywordEditText,distanceEditText,locationEditText,autoDetect)){
                    String keyword=keywordEditText.getText().toString();
                    String distance=distanceEditText.getText().toString();
                    String category = spinner.getSelectedItem().toString();
                    if(autoDetect.isChecked()){
                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        String url="https://ipinfo.io/?token=556a37763de18b";
                        StringRequest request=new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String latitude = jsonObject.getString("loc").split(",")[0];
                                            String longitude = jsonObject.getString("loc").split(",")[1];
                                            cardView.setVisibility(View.GONE);
                                            result.setVisibility(View.VISIBLE);
                                            showSearchResult(resultTableView,resultProgressBarContainer,noEventContainer,keyword,distance,category,longitude,latitude);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                    }
                                });
                        queue.add(request);
                    }
                    else{
                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+locationEditText.getText().toString()+"&key=AIzaSyBon3YcFzZlcOEqA_1VX8gq9i5Iy7lb-io";
                        StringRequest request = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Handle the response here
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String latitude = Double.toString(jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                                            String longitude = Double.toString(jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                                            // Use the latitude and longitude values here
//
                                            cardView.setVisibility(View.GONE);
                                            result.setVisibility(View.VISIBLE);
                                            showSearchResult(resultTableView,resultProgressBarContainer,noEventContainer,keyword,distance,category,longitude,latitude);
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
                    }}
                else{
                    Snackbar snackbar=Snackbar.make(view,"Please fill all fields",Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.snackBar));
                    TextView snackBarTextView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                    snackBarTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    snackbar.show();
                }
            }
        });

        AppCompatButton clear=view.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChangedText=false;
                keywordEditText.setText("");
                locationEditText.setText("");
                spinner.setSelection(0);
                distanceEditText.setText("10");
                autoDetect.setChecked(false);
            }
        });

        keywordEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Send API request to fetch suggestions

                if(userChangedText && !charSequence.toString().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    String searchText = charSequence.toString();
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String url = "https://csci571hw8-4869.wl.r.appspot.com/autocomplete/" + searchText;
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                                @Override
                                public void onResponse(JSONArray response) {
                                    // Handle the JSON array response here
                                    try {
                                        ArrayList<String> suggestionList = new ArrayList<>();
                                        suggestionList.clear();
                                        for (int i = 0; i < response.length(); i++) {
                                            String string = response.getString(i);
                                            suggestionList.add(string);
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.custom_autocomplete_item, suggestionList) {
                                            @NonNull
                                            @Override
                                            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                                View view = super.getView(position, convertView, parent);

                                                // Set the click listener for the view
                                                view.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        // Do nothing on click
                                                        userChangedText=false;
                                                        String selectedItem = getItem(position);

                                                        // Set the text of the AutoCompleteTextView to the selected item
                                                        keywordEditText.setText(selectedItem);

                                                        // Dismiss the dropdown menu
                                                        keywordEditText.dismissDropDown();
                                                    }
                                                });

                                                return view;
                                            }
                                        };

                                        progressBar.setVisibility(View.GONE);
                                        keywordEditText.setAdapter(adapter);
                                        keywordEditText.showDropDown();
                                        // Now you have the list of strings in the stringsList variable
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Handle errors here
                                    error.printStackTrace();
                                }
                            });
                    queue.add(jsonArrayRequest);
                }
                else{
                    userChangedText=true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        AppCompatButton backButton=view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultTableView.setAdapter(new ResultTableAdapter(getContext(),new ArrayList<ResultTableItem>(),view));
                noEventContainer.setVisibility(View.GONE);
                result.setVisibility(View.GONE);
                cardView.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}