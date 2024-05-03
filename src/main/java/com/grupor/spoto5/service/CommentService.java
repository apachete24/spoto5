package com.grupor.spoto5.service;

import com.grupor.spoto5.model.*;
import com.grupor.spoto5.repository.CommentRepository;
import com.grupor.spoto5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CommentService {


    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private UserRepository userRepository;



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
    public void addComment (Comment comment, Long idAlbum) {

        Optional <Album> albumAux = albumService.findById(idAlbum);

        if (albumAux.isPresent()) {

            comment.setAlbum(albumAux.get());
            // validate userName
            if (comment.getUser() == null) {
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

        } else {

            throw new IllegalArgumentException("Album not found");
        }
    }


    // Remove comment
    public void deleteComment(long commentId, User currentUser, boolean isAdmin) {

        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            User userComment = comment.getUser();
            if (currentUser.equals(userComment) || isAdmin) {
                commentRepository.deleteById(commentId);
            } else {
                throw new AccessDeniedException("Permission Denied");
            }
        } else {
            throw new NoSuchElementException("Comment with id " + commentId + " not found");
        }
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
