package com.grupor.spoto5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Album {

    // Atributes

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String artist;
    private String title;
    private Integer release_year;
    @Column(columnDefinition = "TEXT")
    private String text;

    private String image;

    @Lob
    @JsonIgnore
    private Blob imageFile;

    private String videoPath;

    @Transient
    @JsonIgnore
    private MultipartFile videoFile;

    @OneToMany(mappedBy="album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @JsonIgnore
    @ManyToMany
    private List<User> userFavs;


    // private ConcurrentMap<Long, Comment> comments = new ConcurrentHashMap<>();


    // private ArrayList<Long> userFavs = new ArrayList<>();


    // Constructor
    public Album() {
        this.comments = new ArrayList<Comment>();
    }
    // Constructor
    public Album(String artist, String title, Integer release_year, String text) {
        this.artist = artist;
        this.title = title;
        this.release_year = release_year;
        this.text = text;
        this.comments = new ArrayList<Comment>();
        this.videoFile = null;
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

    public Integer getRelease_year() {
        return release_year;
    }

    public void setRelease_year(Integer release_year) {
        this.release_year = release_year;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // Comments
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments (List<Comment> comments) {
        this.comments = comments;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }
    
    /*
    public Comment getComment(Long commentId) {
        return this.comments.get(commentId);
    }
    */

    public void addComment(Comment comment) {
        //this.comments.put(comment.getId(), comment);
        comments.add(comment);
        comment.setAlbum(this);
    }

    public void deleteComment(Comment comment) {
        //this.comments.remove(commentId);
        comments.remove(comment);
        comment.setAlbum(null);
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public MultipartFile getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(MultipartFile videoFile) {
        this.videoFile = videoFile;
    }



    public List<User> getUserFavs() {
        return userFavs;
    }

    public void setUserFavs(List<User> userFavs) {
        this.userFavs = userFavs;
    }

    /*
    // UserFavs
    public ArrayList<Long> getUserFavs() {
        return userFavs;
    }

    public void setUserFavs(ArrayList<Long> userFavs) {
        this.userFavs = userFavs;
    }

    public void addUserFav(Long userId) {
        userFavs.add(userId);
    }

    public void removeUserFav(Long userId) {
        userFavs.remove(userId);
    }

    public boolean isUserFav(Long userId) {
        return userFavs.contains(userId);
    }
    */


}