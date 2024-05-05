package com.grupor.spoto5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;


@Entity(name = "USERS")
public class User {


    // Atributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String encodedPassword;

    @OneToMany(mappedBy= "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    // Favorites albums

    @ManyToMany (mappedBy = "userFavs")
    private List<Album> albumFavs;


    // Constructors
    public User() {
    }

    public User(String name, String encodedPassword, String... roles) {
        this.name = name;
        this.encodedPassword = encodedPassword;
        this.roles = List.of(roles);
    }


    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Album> getAlbumFavs() {
        return albumFavs;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public List<String> getRoles() {
        return roles;
    }

    // Setters
    public void setId(Long idUser) {
        this.id = idUser;
    }

    public void setName(String userName) {
        this.name = userName;
    }

    public void setAlbumFavs(List<Album> albumFavs) {
        this.albumFavs = albumFavs;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


}
