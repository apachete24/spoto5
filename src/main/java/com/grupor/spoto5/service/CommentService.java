package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.Comment;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CommentService {

    AlbumService albumService;

    private AtomicLong nextId = new AtomicLong();

    public CommentService () {

    }

    public Comment findById (long idAlbum, long idComment) {
        return albumService.findById(idAlbum).getComments().get(idComment);
    }

    public Collection<Comment> findAll (Long id) {
        Album album = albumService.findById(id);
        return album.getComments().values();
    }

    public void deleteById (long idAlbum, long idComment) {
        albumService.findById(idAlbum).deleteComment(findById(idAlbum, idComment));
    }

    public void save(long idAlbum, Comment comment) {
        long id = nextId.getAndIncrement();
        comment.setId(id);
        albumService.findById(idAlbum).addComment(comment);
    }
}
