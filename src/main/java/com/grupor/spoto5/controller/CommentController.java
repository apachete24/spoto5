package com.grupor.spoto5.controller;

import com.grupor.spoto5.model.Comment;
import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.User;
import com.grupor.spoto5.repository.UserRepository;
import com.grupor.spoto5.service.AlbumService;
import com.grupor.spoto5.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
public class CommentController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentService commentService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if(principal != null) {

            model.addAttribute("logged", true);
            model.addAttribute("currentUser", principal.getName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
            User user = userRepository.findByName(principal.getName()).orElseThrow();
            model.addAttribute("user", user);

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

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/addcomment/{id}")
    public String newComment(@RequestParam int score, @RequestParam String text, @PathVariable long id, Model model) {

        boolean isAdmin = (boolean) model.getAttribute("admin");
        boolean logged = (boolean) model.getAttribute("logged");

        if (!logged) {
            return "denied";
        }

        try {
            Comment newComment = new Comment((User) model.getAttribute("user"), score, text);
            commentService.addComment(newComment, id);
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

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/deleteComment/{id}")
    public String deleteComment(Model model, @PathVariable long id) {

        Comment comm = commentService.getComment(id).orElseThrow();
        Long idAlbum = comm.getAlbum().getId();

        try {

            User user = (User) model.getAttribute("user");
            Boolean isAdmin = (Boolean) model.getAttribute("admin");

            if (!isAdmin && !user.equals(comm.getUser())) {
                return "denied";
            }

            commentService.deleteComment(id, user, isAdmin);

        } catch (IllegalArgumentException ex) {
            return "redirect:/error";
        }

        return "redirect:/album/" + idAlbum;
    }

}
