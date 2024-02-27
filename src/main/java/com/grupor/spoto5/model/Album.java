package com.grupor.spoto5.model;

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

    private AtomicLong nextId = new AtomicLong();

    public Album() {

    }

    public Album(String artist, String title, Integer year, String text) {
        super();
        this.artist = artist;
        this.title = title;
        this.year = year;
        this.text = text;
    }

    public void addComment (Comment comment) {
        this.comments.put(comment.getId(), comment);
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

    @Override
    public String toString() {
        return "Post [id="+id+", artist=" + artist + ", title=" + title + ", year=" + year + ", text=" + text + "]";
    }
}
