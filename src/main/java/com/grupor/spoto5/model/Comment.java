package com.grupor.spoto5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;



@Entity
public class Comment {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private User user;

    private int score;

    private String text;

    @ManyToOne
    @JsonIgnore
    private Album album;




    public Comment() {
    }

    public Comment (User user, int score, String text) {
        super();
        this.user = user;
        this.score = score;
        this.text = text;
    }


    public Long getId() {
        return id;
    }


    public User getUser() {
        return user;
    }


    public int getScore() {
        return score;
    }


    public String getText() {
        return text;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public void setScore(int score) {
        this.score = score;
    }


    public void setText(String text) {
        this.text = text;
    }


    public Album getAlbum() {
        return album;
    }


    public void setAlbum (Album album) {
        this.album = album;
    }


}
