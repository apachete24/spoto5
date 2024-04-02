package com.grupor.spoto5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private int score;
    private String text;

    @ManyToOne
    @JsonIgnore
    private Album album;


    public Comment() {
    }

    public Comment (String username, int score, String text) {
        super();
        this.username = username;
        this.score = score;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
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

    public void setUsername(String user) {
        this.username = user;
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
