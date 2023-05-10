package com.csci571.hw9.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csci571.hw9.R;
import com.csci571.hw9.models.EventDetail;
import com.csci571.hw9.models.VenueDetail;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VenueFragment extends Fragment implements OnMapReadyCallback {
    private VenueDetail venueDetail;
    private GoogleMap googleMap;
    private MapView mapView;
    private final int maxLine=3;


    public String getAddress(String inputString){
        if(inputString.equals("")){
            return inputString;
        }
        String[] parts = inputString.split(",");
        String firstPart = parts[0].trim();
        return firstPart;
    }

    public String getCity(String inputString){
        if(inputString.equals("")){
            return inputString;
        }
        String[] parts = inputString.split(",");
        String secondPart = parts[1].trim() + ", " + parts[2].trim();
        return secondPart;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        if (args != null) {
            venueDetail = (VenueDetail) args.getSerializable("data");
        }
        View view= inflater.inflate(R.layout.fragment_venue, container, false);
        String formatAddress=getAddress(venueDetail.getAddress());
        String formatCity=getCity(venueDetail.getAddress());
        if(venueDetail.getName().equals("")){
            LinearLayout venueNameContainer=view.findViewById(R.id.venue_name_container);
            venueNameContainer.setVisibility(View.GONE);
        }
        else{
            TextView venueName=view.findViewById(R.id.venue_name);
            venueName.setText(venueDetail.getName());
            venueName.setSelected(true);
        }

        if(formatAddress.equals("")){
            LinearLayout venueAddressContainer=view.findViewById(R.id.venue_address_container);
            venueAddressContainer.setVisibility(View.GONE);
        }
        else{
            TextView venueAddress=view.findViewById(R.id.venue_address);
            venueAddress.setText(formatAddress);
            venueAddress.setSelected(true);
        }
        if(formatCity.equals("")){
            LinearLayout venueCityContainer=view.findViewById(R.id.venue_city_container);
            venueCityContainer.setVisibility(View.GONE);
        }
        else{
            TextView venueCity=view.findViewById(R.id.venue_city);
            venueCity.setText(formatCity);
            venueCity.setSelected(true);
        }

        if(venueDetail.getPhoneNum().equals("")){
            LinearLayout venueContactContainer=view.findViewById(R.id.venue_contact_container);
            venueContactContainer.setVisibility(View.GONE);
        }
        else{
            TextView venueContact=view.findViewById(R.id.venue_contact);
            venueContact.setText(venueDetail.getPhoneNum());
            venueContact.setSelected(true);
        }

        if (venueDetail.getOpenHours().equals("") && venueDetail.getGeneralRule().equals("")
                && (venueDetail.getChildRule().equals(""))
        ) {
            LinearLayout venueRuleContainer=view.findViewById(R.id.venue_rule_container);
            venueRuleContainer.setVisibility(View.GONE);
        }
        else {

            if (venueDetail.getOpenHours().equals("")) {
                LinearLayout venueOpenHourContainer = view.findViewById(R.id.open_hour_container);
                venueOpenHourContainer.setVisibility(View.GONE);
            } else {
                TextView venueOpenHour = view.findViewById(R.id.open_hour);
                venueOpenHour.setText(venueDetail.getOpenHours());
                venueOpenHour.setMaxLines(maxLine);
                venueOpenHour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (venueOpenHour.getMaxLines() == maxLine) {
                            // Expand the text view to show the full paragraph
                            venueOpenHour.setMaxLines(Integer.MAX_VALUE);
                        } else {
                            // Collapse the text view to show only the first few lines
                            venueOpenHour.setMaxLines(maxLine);
                        }

                    }
                });
            }


            if (venueDetail.getGeneralRule().equals("")) {
                LinearLayout venueGeneralRuleContainer = view.findViewById(R.id.general_rules_container);
                venueGeneralRuleContainer.setVisibility(View.GONE);
            } else {
                TextView venueGeneralRule = view.findViewById(R.id.general_rules);
                venueGeneralRule.setText(venueDetail.getGeneralRule());
                venueGeneralRule.setMaxLines(maxLine);
                venueGeneralRule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (venueGeneralRule.getMaxLines() == maxLine) {
                            // Expand the text view to show the full paragraph
                            venueGeneralRule.setMaxLines(Integer.MAX_VALUE);
                        } else {
                            // Collapse the text view to show only the first few lines
                            venueGeneralRule.setMaxLines(maxLine);
                        }

                    }
                });
            }


            if (venueDetail.getChildRule().equals("")) {
                LinearLayout venueChildRuleContainer = view.findViewById(R.id.child_rules_container);
                venueChildRuleContainer.setVisibility(View.GONE);
            } else {
                TextView venueChildRule = view.findViewById(R.id.child_rules);
                venueChildRule.setText(venueDetail.getChildRule());
                venueChildRule.setMaxLines(maxLine);
                venueChildRule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (venueChildRule.getMaxLines() == maxLine) {
                            // Expand the text view to show the full paragraph
                            venueChildRule.setMaxLines(Integer.MAX_VALUE);
                        } else {
                            // Collapse the text view to show only the first few lines
                            venueChildRule.setMaxLines(maxLine);
                        }

                    }
                });
            }
        }


        //Google map
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Add a marker in Sydney, Australia and move the camera
        double latitude = Double.parseDouble(venueDetail.getLat());
        double longitude = Double.parseDouble(venueDetail.getLng());

// Create a LatLng object using the latitude and longitude values
        LatLng location = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(location));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        float zoomLevel = 16.0f; // This value can be adjusted as needed

// Move the camera to the specified location and zoom level
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
