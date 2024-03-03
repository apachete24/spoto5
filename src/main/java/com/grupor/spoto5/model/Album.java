package com.grupor.spoto5.model;


import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class Album {

    private Long id;
    private String artist;
    private String title;
    private Integer year;
    private String text;

    private ConcurrentMap <Long, Comment> comments = new ConcurrentHashMap<>();

    public Album() {

    }

    public Album(String artist, String title, Integer year, String text) {
        super();
        this.artist = artist;
        this.title = title;
        this.year = year;
        this.text = text;
    }


// Getters
    public Long getId() {
        return id;
    }
    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }


// Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public ConcurrentMap<Long, Comment> getComments() {
        return comments;
    }

    public void addComment (Comment comment) {
        this.comments.put(comment.getId(), comment);
    }

    public void deleteComment (Comment comment) {
        this.comments.remove(comment.getId(), comment);
    }

    @Override
    public String toString() {
        return "Post [id="+id+", artist=" + artist + ", title=" + title + ", year=" + year + ", text=" + text + "]";
    }
}
