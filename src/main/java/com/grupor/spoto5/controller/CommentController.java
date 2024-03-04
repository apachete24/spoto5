package com.grupor.spoto5.controller;

import com.grupor.spoto5.model.Comment;
import com.grupor.spoto5.service.AlbumService;
import com.grupor.spoto5.service.CommentService;
import com.grupor.spoto5.service.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/album")
public class CommentController {

    @Autowired
    private UserSession userSession;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private CommentService commentService;


    @PostMapping("/{id}/comment")
    public String newComment(Model model, Comment comment, @PathVariable long id) {
        userSession.setUser(comment.getUser());
        albumService.addComment(id, comment);
        return "redirect:/album/" + id;
    }

    @GetMapping("/{idAlbum}/delete/comment/{idComment}")
    public String deleteComment(Model model, @PathVariable long idAlbum, @PathVariable long idComment) {
        albumService.deleteComment(idAlbum, idComment);
        return "redirect:/album/" + idAlbum;
    }
}
