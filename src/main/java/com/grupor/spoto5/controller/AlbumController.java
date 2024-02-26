package com.grupor.spoto5.controller;

import jakarta.servlet.http.HttpSession;
import org.example.pagina_prueba.model.Album;
import org.example.pagina_prueba.service.AlbumService;
import org.example.pagina_prueba.service.ImageService;
import org.example.pagina_prueba.service.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@Controller
public class AlbumController {

    private static final String ALBUMS_FOLDER = "albums";

    @Autowired
    private AlbumService albumService;

    @Autowired
    private UserSession userSession;

    @Autowired
    private ImageService imageService;

    @GetMapping("/")
    public String showAlbums(Model model, HttpSession session) {
        model.addAttribute("albums", albumService.findAll());
        model.addAttribute("welcome", session.isNew());

        return "index";
    }

    @GetMapping("/album/new")
    public String newAlbumForm(Model model) {

        model.addAttribute("user", userSession.getUser());

        return "new_album";
    }

    @PostMapping("/album/new")
    public String newAlbum(Model model, Album album, MultipartFile image) throws IOException {

        albumService.save(album);

        imageService.saveImage(ALBUMS_FOLDER, album.getId(), image);

        userSession.setUser(album.getArtist());

        return "saved_album";
    }

    @GetMapping("/album/{id}")
    public String showAlbum(Model model, @PathVariable long id) {

        Album album = albumService.findById(id);

        model.addAttribute("album", album);

        return "show_album";
    }
    @GetMapping ("/album/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable int id) throws MalformedURLException {

        return imageService.createResponseFromImage(ALBUMS_FOLDER, id);
    }

    @GetMapping("album/{id}/delete")
    public String deleteAlbum(Model model, @PathVariable long id) throws IOException {

        albumService.deleteById(id);

        imageService.deleteImage(ALBUMS_FOLDER, id);

        return "deleted_album";
    }

}
