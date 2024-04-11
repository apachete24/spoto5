package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.Comment;
import com.grupor.spoto5.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AlbumService albumService;




    // Get all comments
    public Collection<Comment> getComments () {
        return commentRepository.findAll();
    }

    // Get comments by album
    public List<Comment> getComments(long albumId) {

        Optional<Album> albumOptional = albumService.findById(albumId);

        if (albumOptional.isPresent()) {
            Album album = albumOptional.get();
            return album.getComments();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The album is not valid");
        }
    }


    // Get comment by id
    public Optional<Comment> getComment (long id) {
        return commentRepository.findById(id);
    }


    // New comment
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


    // Remove comment
    public void deleteComment (long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            commentRepository.deleteById(commentId);
        } else {
            throw new IllegalArgumentException("Comment with id " + commentId + " not found");        }
    }


    // Find comments by id
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
