package com.grupor.spoto5.model;

import java.util.ArrayList;

public class User {

    private Long idUser;
    private String userName;
    private ArrayList<Long> albumFavs = new ArrayList<>();


    public User() {
    }

    public User(String userName) {
        this.userName = userName;
    }

    // Getters
    public Long getId() {
        return idUser;
    }

    public String getUserName() {
        return userName;
    }

    public ArrayList<Long> getAlbumFavs() {
        return albumFavs;
    }

    // Setters
    public void setId(Long idUser) {
        this.idUser = idUser;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAlbumFavs(ArrayList<Long> albumFavs) {
        this.albumFavs = albumFavs;
    }

    public void addAlbumFav(Long albumId) {
        albumFavs.add(albumId);
    }

    public void removeAlbumFav(Long albumId) {
        albumFavs.remove(albumId);
    }

    public boolean isAlbumFav(Long albumId) {
        return albumFavs.contains(albumId);
    }


}
