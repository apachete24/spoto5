package com.grupor.spoto5.controller;

import com.grupor.spoto5.model.Comment;
import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.service.AlbumService;
import com.grupor.spoto5.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
public class CommentController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private CommentService commentService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if(principal != null) {

            model.addAttribute("logged", true);
            model.addAttribute("currentUser", principal.getName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));

        } else {
            model.addAttribute("logged", false);
        }
    }

    /*
    @PostMapping("/{id}/comment")
    public String newComment(Model model, Comment comment, @PathVariable long id) {
        userSession.setUser(comment.getUsername());
        commentService.addComment(id, comment);
        return "redirect:/album/" + id;
    }
    */

    @PostMapping("/addcomment/{id}")
    public String newComment(Comment comment, @PathVariable long id, Model model) {

        try {

            Album album = albumService.findById(id).orElseThrow();
            comment.setAlbum(album);
            commentService.addComment(comment);
            return "redirect:/album/" + id;

        } catch (IllegalArgumentException ex) {

            model.addAttribute("error", ex.getMessage());
            return "error";

        }
    }

    /*
    @GetMapping("/{idAlbum}/delete/comment/{idComment}")
    public String deleteComment(Model model, @PathVariable long idAlbum, @PathVariable long idComment) {
        commentService.deleteComment(idAlbum, idComment);
        return "redirect:/album/" + idAlbum;
    }
    */

    @GetMapping("/deleteComment/{idAlbum}/comment/{idComment}")
    public String deleteComment(Model model, @PathVariable long idAlbum, @PathVariable long idComment) {
        try {
            commentService.deleteComment(idComment);
        } catch (IllegalArgumentException ex) {
            return "redirect:/error";
        }
        return "redirect:/album/" + idAlbum;
    }

}
