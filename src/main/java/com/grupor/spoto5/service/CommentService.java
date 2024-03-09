package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.Comment;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CommentService {

    private AlbumService albumService;

    private AtomicLong nextId = new AtomicLong();

    public CommentService(AlbumService albumService) {

        this.albumService = albumService;
    }

    public Collection<Comment> getComments() {
        return albumService.findAll().stream()
                .flatMap(album -> album.getComments().values().stream())
                .toList();
    }
    public Collection<Comment> getComments(long albumId) {
        return albumService.findById(albumId).getComments().values();
    }

    public void addComment(long albumId, Comment comment) {
        Album album = albumService.findById(albumId);
        if (album != null) {
            Long commentId = nextId.getAndIncrement();
            comment.setId(commentId);
            album.addComment(comment);
        }
    }

    public void deleteComment(long albumId, long commentId) {
        Album album = albumService.findById(albumId);
        if (album != null) {
            album.deleteComment(commentId);
        }
    }

}
