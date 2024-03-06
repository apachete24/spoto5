package com.grupor.spoto5.controller;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.service.AlbumService;
import com.grupor.spoto5.service.CommentService;
import com.grupor.spoto5.service.ImageService;
import com.grupor.spoto5.service.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

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
    public String showAlbums(Model model) {
        model.addAttribute("albums", albumService.findAll());
        return "index";
    }

    @GetMapping("/album/{id}")
    public String showAlbum(Model model, @PathVariable long id) {
        Album album = albumService.findById(id);
        model.addAttribute("user", userSession.getUser());
        model.addAttribute("album", album);
        model.addAttribute("comments", commentService.getComments(id));
        return "show_album";
    }


    @GetMapping("/album/new")
    public String newAlbumForm() {
        return "new_album";
    }


    @PostMapping("/album/new")
    public String newAlbum(Model model, Album album, MultipartFile image) throws IOException {

        albumService.save(album);

        imageService.saveImage(ALBUMS_FOLDER, album.getId(), image);

        return "saved_album";
    }

    @GetMapping("/album/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable int id) throws MalformedURLException {
        return imageService.createResponseFromImage(ALBUMS_FOLDER, id);
    }

    @GetMapping("/album/{id}/delete")
    public String deleteAlbum(Model model, @PathVariable long id) throws IOException {
        model.addAttribute(albumService.findById(id));
        albumService.deleteById(id);
        imageService.deleteImage(ALBUMS_FOLDER, id);

        return "deleted_album";
    }

    @GetMapping("/album/{id}/edit")
    public String updateAlbum(Model model, @PathVariable long id) {
        Album album = albumService.findById(id);

        model.addAttribute("album", album);

        return "edit_album";
    }


    @PostMapping("/album/{id}/edit")
    public String updateAlbum(@PathVariable Long id, Album updatedAlbum, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            imageService.saveImage(ALBUMS_FOLDER, id, image);
        }
        albumService.updateAlbum(id, updatedAlbum);
        return "redirect:/album/" + id;
    }


}