package com.grupor.spoto5.controller;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.Comment;
import com.grupor.spoto5.service.AlbumService;
import com.grupor.spoto5.service.CommentService;
import com.grupor.spoto5.service.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserSession userSession;

    @Autowired
    private AlbumService albumService;
/*
    @GetMapping("/album/{id}")
    public String showComments(Model model, @PathVariable long id) {
        Album album = albumService.findById(id);
        model.addAttribute(album.getComments());
        return "show_album";
    }*/

    @PostMapping("/album/{idAlbum}")
    public String addComment(Model model, @PathVariable long idAlbum) {
        Comment comment = new Comment();
        commentService.saveComment(comment);
        return "album/{idAlbum}";
    }

    @GetMapping("album/{idAlbum}/comment/{idComment}/delete")
    public String deleteComment(Model model, @PathVariable long idAlbum, @PathVariable long idComment){

        return "album/{idAlbum}";
    }




}
