package com.grupor.spoto5.model;

import jakarta.persistence.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


@Entity
public class User {


    // Atributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idUser;
    private String userName;
    // Favorites albums

    /*
    @ManyToMany (mappedBy = "user")
    private List<Album> albumFavs;
    */

    // Constructors
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

    /*
    public List<Album> getAlbumFavs() {
        return albumFavs;
    }
    */

    // Setters
    public void setId(Long idUser) {
        this.idUser = idUser;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /*
    public void addAlbumFav(Album newAlbum) {
        albumFavs.add(newAlbum);


    public void removeAlbumFav(Album album) {
        albumFavs.remove(album);
    }

    public boolean isAlbumFav(Album album) {
        return albumFavs.contains(album);
    }
    */

}
