package com.csci571.hw9.models;

import java.io.Serializable;

public class EventDetail implements Serializable {
    String id;
    String name;
    String date;
    String artists;
    String venue;
    String genres;
    String priceRanges;
    String status;
    String ticketUrl;
    String seatmapUrl;
    String time;

    public EventDetail(String id, String name, String date, String time,String artists, String venue, String genres, String priceRanges, String status, String ticketUrl, String seatmapUrl) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time=time;
        this.artists = artists;
        this.venue = venue;
        this.genres = genres;
        this.priceRanges = priceRanges;
        this.status = status;
        this.ticketUrl = ticketUrl;
        this.seatmapUrl = seatmapUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getArtists() {
        return artists;
    }

    public String getVenue() {
        return venue;
    }

    public String getGenres() {
        return genres;
    }

    public String getPriceRanges() {
        return priceRanges;
    }

    public String getStatus() {
        return status;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public String getSeatmapUrl() {
        return seatmapUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setPriceRanges(String priceRanges) {
        this.priceRanges = priceRanges;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public void setSeatmapUrl(String seatmapUrl) {
        this.seatmapUrl = seatmapUrl;
    }

    @Override
    public String toString() {
        return "EventDetail{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", artists='" + artists + '\'' +
                ", venue='" + venue + '\'' +
                ", genres='" + genres + '\'' +
                ", priceRanges='" + priceRanges + '\'' +
                ", status='" + status + '\'' +
                ", ticketUrl='" + ticketUrl + '\'' +
                ", seatmapUrl='" + seatmapUrl + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
