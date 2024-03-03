package com.grupor.spoto5.model;

public class Comment {
    private Long id;
    private String user;
    private int score;
    private String text;

    public Comment() {
    }

    public Comment (String user, int score, String text) {
        super();
        this.user = user;
        this.score = score;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public String getUser() {
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

    public void setUser(String user) {
        this.user = user;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setText(String text) {
        this.text = text;
    }


}
