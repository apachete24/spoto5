package com.grupor.spoto5.model;

public class Comment {

    private Long id;

    private String user;

    private Integer score;

    private String text;


    public Comment (String user, Integer score, String text) {
        this.user = user;
        this.score = score;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
