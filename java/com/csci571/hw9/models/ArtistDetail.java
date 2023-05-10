package com.csci571.hw9.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArtistDetail implements Serializable {
    String name;
    String artistImg;
    String popularity;
    String followers;
    String spotifyUrl;
    List<String> albums;

    public ArtistDetail(String name, String artistImg, String popularity, String followers, String spotifyUrl, List<String> albums) {
        this.name = name;
        this.artistImg = artistImg;
        this.popularity = popularity;
        this.followers = followers;
        this.spotifyUrl = spotifyUrl;
        this.albums = albums;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtistImg() {
        return artistImg;
    }

    public void setArtistImg(String artistImg) {
        this.artistImg = artistImg;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }

    public List<String> getAlbums() {
        return albums;
    }

    public void setAlbums(List<String> albums) {
        this.albums = albums;
    }

    @Override
    public String toString() {
        return "ArtistDetail{" +
                "name='" + name + '\'' +
                ", artistImg='" + artistImg + '\'' +
                ", popularity='" + popularity + '\'' +
                ", followers='" + followers + '\'' +
                ", spotifyUrl='" + spotifyUrl + '\'' +
                ", albums=" + albums +
                '}';
    }
}
