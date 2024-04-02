package com.grupor.spoto5.restControler;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.Comment;
import com.grupor.spoto5.service.AlbumService;
import com.grupor.spoto5.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api")
public class CommentRESTController {

    /*
    @Autowired
    private CommentService comments;
    @Autowired
    private AlbumService albums;
    // Get all comments
    @GetMapping("/comments")
    public Collection<Comment> getComments() {
        return comments.getComments();
    }

    // Get comment by id
    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable long id) {
        Comment comment = comments.getComment(id);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Get comments by album
    @GetMapping("/albums/{albumId}/comments")
    public ResponseEntity<Collection<Comment>> getComments(@PathVariable long albumId) {
        Album album = albums.findById(albumId);
        if (album != null){
            Collection<Comment> comments = album.getComments().values();
            return ResponseEntity.ok(comments);
        } else {
            return ResponseEntity.notFound().build();
        }
        
    }
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
