package com.grupor.spoto5.restControler;
import  com.grupor.spoto5.service.AlbumService;
import  com.grupor.spoto5.model.Album;

import com.grupor.spoto5.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api")
public class AlbumRESTController{


    @Autowired
    private AlbumService albums;
    @Autowired
    private ImageService images;

    @GetMapping("/albums")
    public Collection<Album> getAlbums() {
        return albums.findAll();

    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<Album> getAlbum(@PathVariable long id){

        Album album = albums.findById(id);
        if (album != null) {
            return ResponseEntity.ok(album);
        } else {
            return ResponseEntity.notFound().build();
        }
        
    }

    @PostMapping("/albums")
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {

        albums.save(album);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(album.getId()).toUri();

        return ResponseEntity.created(location).body(album);
    }


    @PutMapping("/albums/{id}") // NOT WORKING PROPERLY
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

    @DeleteMapping("/albums/{id}")
    public ResponseEntity<Album> deleteAlbum(@PathVariable long id) {

        Album album = albums.findById(id);

        if (album != null) {
            albums.deleteById(id);
            return ResponseEntity.ok(album);
        } else {
            return ResponseEntity.notFound().build();
        }
    }








}
