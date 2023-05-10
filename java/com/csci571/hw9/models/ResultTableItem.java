package com.csci571.hw9.models;

import java.io.Serializable;

public class ResultTableItem implements Serializable {
    String id;
    String imgSrc;
    String event;
    String venue;
    String category;
    String date;
    String time;
    boolean isFavorite;



    public ResultTableItem(String id,String imgSrc, String event, String venue, String category, String date, String time, boolean isFavorite) {
        this.imgSrc = imgSrc;
        this.id=id;
        this.event = event;
        this.venue = venue;
        this.category = category;
        this.date = date;
        this.time = time;
        this.isFavorite = isFavorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public String getEvent() {
        return event;
    }

    public String getVenue() {
        return venue;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public String toString() {
        return "ResultTableItem{" +
                "id='" + id + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                ", event='" + event + '\'' +
                ", venue='" + venue + '\'' +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
