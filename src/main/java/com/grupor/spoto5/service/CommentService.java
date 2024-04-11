package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.Comment;
import com.grupor.spoto5.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CommentService {

    // private AlbumService albumService;

    private CommentRepository commentRepository;

    private AlbumService albumService;

    // private AtomicLong nextId = new AtomicLong();

    public CommentService (CommentRepository commentRepository, AlbumService albumService) {
        this.albumService = albumService;
        this.commentRepository = commentRepository;
    }

    /*
    public CommentService(AlbumService albumService) {

        this.albumService = albumService;
    }
    */

    /*
    public Collection<Comment> getComments() {
        return albumService.findAll().stream()
                .flatMap(album -> album.getComments().values().stream())
                .toList();
    }
    */

    public Collection<Comment> getComments () {
        return commentRepository.findAll();
    }

    /*
    public Collection<Comment> getComments(long albumId) {
        return albumService.findById(albumId).getComments().values();
    }
    */

    public List<Comment> getComments(long albumId) {

        Optional<Album> albumOptional = albumService.findById(albumId);

        if (albumOptional.isPresent()) {
            Album album = albumOptional.get();
            return album.getComments();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The album is not valid");
        }
    }

    /*
    public Comment getComment(long id){
        for (Comment comment : getComments()) {
            if (comment.getId() == id) {
                return comment;
            }
        }
        return null;
    }
    */

    public Optional<Comment> getComment (long id) {
        return commentRepository.findById(id);
    }

    /*
    public void addComment(long albumId, Comment comment) {
        Album album = albumService.findById(albumId);
        if (album != null) {
            Long commentId = nextId.getAndIncrement();
            comment.setId(commentId);
            album.addComment(comment);
        }
    }
    */

    public void addComment (Comment comment) {

        // validate userName
        if (comment.getUsername() == null || comment.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        // validate Score
        if (comment.getScore() < 0 || comment.getScore() > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100.");
        }
        // Validate Text
        if (comment.getText() == null || comment.getText().isEmpty()) {
            throw new IllegalArgumentException("Comment text cannot be empty.");
        }
        // If comment is valid
        this.commentRepository.save(comment);
    }

    /*
    public void deleteComment(long albumId, long commentId) {
        Album album = albumService.findById(albumId);
        if (album != null) {
            album.deleteComment(commentId);
        }
    }
    */
    public void deleteComment (long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            commentRepository.deleteById(commentId);
        } else {
            throw new IllegalArgumentException("Comment with id " + commentId + " not found");        }
    }

    public List<Comment> findByIds(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("IDs cannot be empty.");
        }

        if (ids.stream().anyMatch(id -> id <= 0)) {
            throw new IllegalArgumentException("IDs must be greater than 0");
        }

        return commentRepository.findAllById(ids);
    }

}
