package com.grupor.spoto5.restControler;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.Comment;
import com.grupor.spoto5.service.AlbumService;
import com.grupor.spoto5.service.CommentService;
import com.grupor.spoto5.service.UserSession;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/comments")
public class CommentRESTController {

    @Autowired
    private UserSession userSession;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AlbumService albumService;


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
    @GetMapping("/albums/{albumId}")
    public ResponseEntity<Collection<Comment>> getComments(@PathVariable long albumId) {

        Optional<Album> album = albumService.findById(albumId);
        if (album.isPresent()) {
            return ResponseEntity.ok(album.get().getComments());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    /*
    // Add comment to album
    @PostMapping("/albums/{albumId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable long albumId, @RequestBody Comment comment) {
        comments.addComment(albumId, comment);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(comment.getId()).toUri();

        return ResponseEntity.created(location).body(comment);
    }

    // Delete comment
    @DeleteMapping("/albums/{albumId}/comments/{commentId}")
    public ResponseEntity<Comment> deleteComment(@PathVariable long albumId, @PathVariable long commentId) {

        Album album = albums.findById(albumId);
        if (album != null) {
           Comment comment = album.getComment(commentId);
           if (comment != null) {
               // If album and comment exists
               comments.deleteComment(albumId, commentId);
               return ResponseEntity.ok(comment);
           }
        }
        // if comment or album does not exist
        return ResponseEntity.notFound().build();

    }

    // Update comment
    @PutMapping("/albums/{albumId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable long albumId, @PathVariable long commentId, @RequestBody Comment newComment) {
        Album album = albums.findById(albumId);
        if (album != null) {
            Comment comment = album.getComment(commentId);
            if (comment != null) {
                newComment.setId(commentId);
                // pending of review and improvement
                comments.deleteComment(albumId, commentId);
                comments.addComment(albumId, newComment);
                return ResponseEntity.ok(newComment);
            }
        }
        return ResponseEntity.notFound().build();
    }


    */

}
