package com.grupor.spoto5.controller;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.service.AlbumService;
import com.grupor.spoto5.service.CommentService;
import com.grupor.spoto5.service.ImageService;
import com.grupor.spoto5.service.UserSession;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@Controller
public class AlbumController {

    private static final String ALBUMS_FOLDER = "albums";

    @Autowired
    private UserSession userSession;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String showAlbums(Model model, @RequestParam(required = false) Integer from, @RequestParam(required = false) Integer to) {

        model.addAttribute("albums", albumService.findAll(from, to));

        return "index";
    }

    /*
    @GetMapping("/album/{id}")
    public String showAlbum(Model model, @PathVariable long id) {
        Album album = albumService.findById(id);
        if (album != null) {
            model.addAttribute("user", userSession.getUser());
            model.addAttribute("album", album);
            model.addAttribute("comments", commentService.getComments(id));
            return "show_album";
        } else {
            return "error";
        }
    }
    */
    @GetMapping("/album/{id}")
    public String showAlbum(Model model, @PathVariable long id) {
        Optional<Album> album = albumService.findById(id);
        if (album.isPresent()) {
            model.addAttribute("user", userSession.getUser());
            model.addAttribute("album", album.get());
            model.addAttribute("comments", commentService.getComments(id)); // ?????????
            return "show_album";
        } else {
            return "error";
        }
    }

    @GetMapping("/album/new")
    public String newAlbumForm() {
        return "new_album";
    }


    @PostMapping("/album/new")
    public String newAlbum(Album album, @RequestParam MultipartFile image) throws IOException {

        /*
        URI location = fromCurrentRequest().build().toUri();

        album.setImage(location.toString());
        album.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.getSize()));
        albumService.save(album);
        */
        String file = imageService.createImage(image);
        album.setImage(file);
        albumService.save(album);
        return "saved_album";
    }


    @GetMapping("/album/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable int id) throws SQLException {

        // return imageService.createResponseFromImage(ALBUMS_FOLDER, id);

        Optional<Album> al = albumService.findById(id);

        if (al.isPresent()) {
            Album album = al.get();
            Resource poster = imageService.getImage(album.getImage());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpg").body(poster);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found");
        }
    }




    @GetMapping("/album/{id}/delete")
    public String deleteAlbum(Model model, @PathVariable long id) throws IOException {
        // model.addAttribute(albumService.findById(id));
        imageService.deleteImage(albumService.findById(id).get().getImage());
        albumService.deleteById(id);

        return "deleted_album";
    }


    @GetMapping("/album/{id}/edit")
    public String updateAlbum(Model model, @PathVariable long id) {

        Optional<Album> optionalAlbum = albumService.findById(id);

        if (optionalAlbum.isPresent()) {
            Album album = optionalAlbum.get();
            model.addAttribute("album", album);
            return "edit_album";
        } else {
            return "error";
        }
    }



    /*
    @PostMapping("/album/{id}/edit")
    public String updateAlbum(@PathVariable Long id, Album updatedAlbum, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            imageService.saveImage(ALBUMS_FOLDER, id, image);
        }
        albumService.updateAlbum(id, updatedAlbum);
        return "redirect:/album/" + id;
    }
    */

    /*
    @PostMapping("/album/{id}/edit")
    public String updateAlbum(@PathVariable Long id, Album updatedAlbum, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            imageService.saveImage(ALBUMS_FOLDER, id, image);
        }
        updatedAlbum.setId(id);
        albumService.updateAlbum(updatedAlbum);
        return "redirect:/album/" + id;
    }
    */
}