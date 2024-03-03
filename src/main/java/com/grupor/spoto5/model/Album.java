package com.grupor.spoto5.model;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class Album {


    private Long id;
    private String artist;
    private String title;
    private Integer year;
    private String text;

    private ConcurrentMap<Long, Comment> comments = new ConcurrentHashMap<>();

    public Album() {
    }

    public Album(String artist, String title, Integer year, String text) {
        this.artist = artist;
        this.title = title;
        this.year = year;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void addComment(Comment comment) {
        this.comments.put(comment.getId(), comment);
    }

    public void deleteComment(Long commentId) {
        this.comments.remove(commentId);
    }

    // Resto del código...

    public ConcurrentMap<Long, Comment> getComments() {
        return comments;
    }
}
