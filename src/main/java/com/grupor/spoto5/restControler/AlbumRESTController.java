package com.grupor.spoto5.restControler;
import  com.grupor.spoto5.service.AlbumService;
import  com.grupor.spoto5.model.Album;

import com.grupor.spoto5.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/albums")
public class AlbumRESTController{


    @Autowired
    private AlbumService albums;
    @Autowired
    private ImageService images;

    @GetMapping("")
    public Collection<Album> getAlbums() {
        return albums.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbum(@PathVariable long id){

        Album album = albums.findById(id);
        if (album != null) {
            return ResponseEntity.ok(album);
        } else {
            return ResponseEntity.notFound().build();
        }
        
    }

    @PostMapping("")
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) throws IOException {

        albums.save(album);
        // images.saveImage("albums", album.getId(), imageFile); I don't know if the image and the JSON data can be sent in the same request
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(album.getId()).toUri();

        return ResponseEntity.created(location).body(album);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable long id, @RequestBody Album newAlbum) {

        Album album = albums.findById(id);

        if (album != null) {
            newAlbum.setId(id);
            albums.save(newAlbum);

            return ResponseEntity.ok(newAlbum);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Album> deleteAlbum(@PathVariable long id) throws IOException {

        Album album = albums.findById(id);

        if (album != null) {
            albums.deleteById(id);
            images.deleteImage("albums", id);
            return ResponseEntity.ok(album);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


// Operations with album images

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws IOException {

        if (albums.findById(id)!=null) {
            return images.createResponseFromImage("albums", id);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Object> uploadImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {

        if (albums.findById(id)!=null) {
            images.saveImage("albums", id, imageFile);

            return ResponseEntity.ok().build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteImage(@PathVariable long id) throws IOException {

        if (albums.findById(id)!=null) {
            images.deleteImage("albums", id);

            return ResponseEntity.ok().build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
