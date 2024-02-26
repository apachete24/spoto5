package com.grupor.spoto5.model;

public class Comment {

    private Long id;

    private String user;

    private Integer score;

    private String text;

    public Comment () {

    }

    public Comment (String user, Integer mark, String text) {
        super();
        this.user = user;
        this.score = mark;
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

    public Integer getMark() {
        return score;
    }

    public void setMark(Integer mark) {
        this.score = mark;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
