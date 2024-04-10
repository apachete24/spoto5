package com.grupor.spoto5.controller;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.service.*;
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
import java.sql.Blob;
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

    @Autowired
    private VideoService videoService;

    @GetMapping("/")
    public String showAlbums(Model model, @RequestParam(required = false) Integer from, @RequestParam(required = false) Integer to) {

        model.addAttribute("albums", albumService.findAll(from, to));

        return "index";
    }

    @GetMapping("/album/{id}")
    public String showAlbum(Model model, @PathVariable long id) {
        Optional<Album> album = albumService.findById(id);
        if (album.isPresent()) {
            model.addAttribute("user", userSession.getUser());
            model.addAttribute("album", album.get());
            model.addAttribute("comments", commentService.getComments(id));
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
    public String newAlbum(Album album, @RequestParam MultipartFile albumImage, @RequestParam(required = false) MultipartFile albumVideo) throws IOException {
        albumService.save(album, albumImage, albumVideo);
        return "saved_album";
    }



    @GetMapping("/album/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable int id) throws SQLException {

        Optional<Album> al = albumService.findById(id);

        if (al.isPresent()) {
            Album album = al.get();
            Resource poster = imageService.getImage(album.getImage());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpg").body(poster);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found");
        }
    }

    @GetMapping("/album/{id}/video")
    public ResponseEntity<Resource> downloadVideo(@PathVariable long id) {
        Optional<Album> albumOptional = albumService.findById(id);

        if (albumOptional.isPresent()) {
            Album album = albumOptional.get();

            if (album.getVideoPath() != null && !album.getVideoPath().isEmpty()) {
                Resource video = (Resource) videoService.getVideo(album.getVideoPath());

                if (video != null) {
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                            .body(video);
                }
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found");
    }


    @GetMapping("/album/{id}/delete")
    public String deleteAlbum(@PathVariable long id) throws SQLException {

        Optional <Album> al = albumService.findById(id);
        if (al.isPresent()) {
            Album album = al.get();
            if (album.getImage() != null) {
                imageService.deleteImage(album.getImage());
            }
            if (album.getImageFile() != null) {
                album.getImageFile().free();
            }
            albumService.deleteById(id);
        } else {
            return "error";
        }
        return "deleted_album";
    }


    @GetMapping("/album/{id}/edit")
    public String updateAlbum(Model model, @PathVariable long id) {

        Optional<Album> optionalAlbum = albumService.findById(id);

        if (optionalAlbum.isPresent()) {
            Album album = optionalAlbum.get();
            model.addAttribute("album", album);

            model.addAttribute("imageFileName", album.getImage());

            return "edit_album";
        } else {
            return "error";
        }
    }

    @PostMapping("/album/{id}/edit")
    public String updateAlbum(@PathVariable Long id, Album updatedAlbum, @RequestParam(required = false) MultipartFile albumImage, @RequestParam(required = false) MultipartFile albumVideo) throws IOException {
        if (albumImage != null && !albumImage.isEmpty()) {
            String fileImage = imageService.createImage(albumImage);
            updatedAlbum.setImage(fileImage);
            updatedAlbum.setImageFile(BlobProxy.generateProxy(albumImage.getInputStream(), albumImage.getSize()));
        }
        if (albumVideo != null && !albumVideo.isEmpty()) {
            String fileVideo = videoService.createVideo(albumVideo);
            updatedAlbum.setVideoPath(fileVideo);
        }
        albumService.updateAlbum(id, updatedAlbum);
        return "redirect:/album/" + id;
    }

}