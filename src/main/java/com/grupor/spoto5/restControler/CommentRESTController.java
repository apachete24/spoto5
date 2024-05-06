package com.grupor.spoto5.restControler;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.Comment;
import com.grupor.spoto5.model.User;
import com.grupor.spoto5.repository.UserRepository;
import com.grupor.spoto5.service.AlbumService;
import com.grupor.spoto5.service.CommentService;
import com.grupor.spoto5.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/comments")
public class CommentRESTController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRESTController userRESTController;

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

    // Get all comments
    @GetMapping("")
    public Collection<Comment> getComments() {
        return commentService.getComments();
    }


    // Get comment by id
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable long id) {
        Optional<Comment> comment = commentService.getComment(id);
        if (comment.isPresent()) {
            return ResponseEntity.ok(comment.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Get comments by album
    @GetMapping("/albums/{albumId}/comments")
    public ResponseEntity<Collection<Comment>> getComments(@PathVariable long albumId) {

        Optional<Album> album = albumService.findById(albumId);
        if (album.isPresent()) {
            return ResponseEntity.ok(album.get().getComments());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    // Add comment to album
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/albums/{albumId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable long albumId, @RequestBody Comment comment, Model model) {

        Optional<Album> album = albumService.findById(albumId);
        if (album.isPresent()) {
            comment.setAlbum(album.get());
            User autor = (userService.findByName((String)model.getAttribute("currentUser"))).get();
            comment.setUser(autor);
            commentService.addComment(comment, albumId);
            URI location = fromCurrentRequest().path("/{id}").buildAndExpand(comment.getId()).toUri();
            return ResponseEntity.created(location).body(comment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Delete comment by id
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Comment> deleteComment(Model model, @PathVariable long commentId) {

        Optional<Comment> comment = commentService.getComment(commentId);
        if (comment.isPresent()) {
            User user = (User) model.getAttribute("user");Boolean isAdmin = (Boolean) model.getAttribute("admin");
            try{commentService.deleteComment(commentId, user, isAdmin);} catch (Exception e){
                return ResponseEntity.status(405).build();
            }
                return ResponseEntity.ok().build();

        } else {
            return ResponseEntity.notFound().build();
        }

    }


    // Update comment
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(Model model, @PathVariable long commentId, @RequestBody Comment updatedComment) {


        Optional<Comment> isComment = commentService.getComment(commentId);

        if (isComment.isPresent()) {

            Comment comment = isComment.get();
            User autor = comment.getUser();

            // Validate if the current user is Admin or the comment's autor
            if ( (boolean) model.getAttribute("admin") ||
                    autor.getId() == userService.findByName((String)model.getAttribute("currentUser")).get().getId() ){

                // User is not allowed to change the username and id comment
                updatedComment.setId(commentId);
                updatedComment.setUser(comment.getUser());
                try { commentService.addComment(updatedComment, null);} catch (Exception e){
                    ResponseEntity.status(405).build();
                }

                return ResponseEntity.ok(updatedComment);

            } else{
                return ResponseEntity.status(405).build();
            }

        } else {
            return ResponseEntity.notFound().build();
        }
    }




}
