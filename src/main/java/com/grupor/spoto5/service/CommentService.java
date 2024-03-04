package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.Comment;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class CommentService {

    private AlbumService albumService;

    private AtomicLong nextId = new AtomicLong();

    public CommentService(AlbumService albumService) {

        this.albumService = albumService;
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
