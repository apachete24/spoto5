package com.grupor.spoto5.restControler;
import com.grupor.spoto5.model.Comment;
import com.grupor.spoto5.model.User;
import com.grupor.spoto5.service.*;
import  com.grupor.spoto5.model.Album;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/albums")
public class AlbumRESTController{

    @Autowired
    private UserSession userSession;

    @Autowired
    private UserService userService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private VideoService videoService;

    // Get all albums if no parameters are passed, or get albums between from and to years.
    @GetMapping("")
    public Collection<Album> getAlbums(Model model, @RequestBody(required = false) Integer from, @RequestBody(required = false) Integer to) {

        return albumService.findAll(from, to);

    }


    // Get album by id
    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbum(@PathVariable long id){
        Optional<Album> album = albumService.findById(id);
        if (album.isPresent()) { // album found
            return ResponseEntity.ok(album.get());
        } else { // album not found
            return ResponseEntity.notFound().build();
        }
    }

    // Create album
    @PostMapping("")
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) throws IOException {

        albumService.save(album);
        if (album.getId() != null) { // if it was created successfully
            return ResponseEntity.ok(album);
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

    // Update album
    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable long id, @RequestBody Album newAlbum) {

        Optional<Album> album = albumService.findById(id);
        if (album.isPresent()) {
            album.get().setArtist(newAlbum.getArtist());
            album.get().setTitle(newAlbum.getTitle());
            album.get().setRelease_year(newAlbum.getRelease_year());
            album.get().setText(newAlbum.getText());
            try {
                albumService.save(album.get());
            } catch (IOException e) { // 500 internall error
                throw new RuntimeException(e);
            }
            // Saved successfully
            return ResponseEntity.ok(album.get());

        } else { // album not found
            return ResponseEntity.notFound().build();
        }
    }



    // Delete album
    @DeleteMapping("/{id}")
    public ResponseEntity<Album> deleteAlbum(@PathVariable long id) throws IOException {

        Optional<Album> album = albumService.findById(id);
        if (album.isPresent()) {
            albumService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get comments
    @GetMapping("/{id}/comments")
    public ResponseEntity<Collection<Comment>> getComments(@PathVariable long id) {

        Optional<Album> album = albumService.findById(id);
        if (album.isPresent()) {
            return ResponseEntity.ok(album.get().getComments());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

// Operations with album images

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws IOException {

        Optional<Album> isAlbum = albumService.findById(id);
        if (isAlbum.isPresent()){
            Album album = isAlbum.get();
            Resource poster = imageService.getImage(album.getImage());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpg").body(poster);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Object> uploadImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {

        Optional<Album> isAlbum = albumService.findById(id);
        if (isAlbum.isPresent()) {
            Album album = isAlbum.get();
            String imageName = imageService.createImage(imageFile);
            album.setImage(imageName);
            albumService.save(album);
            URI location = fromCurrentRequest().path("/{id}").buildAndExpand(album.getId()).toUri();
            return ResponseEntity.created(location).body(album);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Delete album image
    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteImage(@PathVariable long id) throws IOException {

        Optional<Album> isAlbum = albumService.findById(id);
        if (isAlbum.isPresent()) {
            Album album = isAlbum.get();
            imageService.deleteImage(album.getImage());
            album.setImage(null);
            albumService.save(album);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Operations with album videos

    @GetMapping("/{id}/video")
    public ResponseEntity<Object> downloadVideo(@PathVariable long id) throws IOException {

        Optional<Album> isAlbum = albumService.findById(id);
        if (isAlbum.isPresent()){
            Album album = isAlbum.get();
            Resource video = videoService.getVideo(album.getVideoPath());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "video/mp4").body(video);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/video")
    public ResponseEntity<Object> uploadVideo(@PathVariable long id, @RequestParam MultipartFile videoFile) throws IOException {

        Optional<Album> isAlbum = albumService.findById(id);
        if (isAlbum.isPresent()) {
            Album album = isAlbum.get();
            String videoPath = videoService.createVideo(videoFile);
            album.setVideoPath(videoPath);
            albumService.save(album);
            URI location = fromCurrentRequest().path("/{id}").buildAndExpand(album.getId()).toUri();
            return ResponseEntity.created(location).body(album);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete album video
    @DeleteMapping("/{id}/video")
    public ResponseEntity<Object> deleteVideo(@PathVariable long id) throws IOException {

        Optional<Album> isAlbum = albumService.findById(id);
        if (isAlbum.isPresent()) {
            Album album = isAlbum.get();
            videoService.deleteVideo(album.getVideoPath());
            album.setVideoPath(null);
            albumService.save(album);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
