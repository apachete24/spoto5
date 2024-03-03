package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.Comment;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AlbumService {

    private ConcurrentMap<Long, Album> albums = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong();

    public AlbumService() {
        // Inicialización con algunos álbumes de ejemplo
        save(new Album("Nico Miseria", "Tercer Verano del Amor", 2022, "Texto del álbum 1"));
        save(new Album("Cruz Cafune", "Me muevo con dios", 2023, "Texto del álbum 2"));
        // Agrega más álbumes según sea necesario
    }

    public Collection<Album> findAll() {
        return albums.values();
    }

    public Album findById(long id) {
        return albums.get(id);
    }

    public void deleteById(long id) {
        albums.remove(id);
    }

    public void save(Album album) {
        long id = nextId.getAndIncrement();
        album.setId(id);
        albums.put(id, album);
    }

    public void addComment(long albumId, Comment comment) {
        Album album = findById(albumId);
        if (album != null) {
            long commentId = nextId.getAndIncrement();
            comment.setId(commentId);
            album.addComment(comment);
        }
    }

    public void deleteComment(long albumId, long commentId) {
        Album album = findById(albumId);
        if (album != null) {
            album.deleteComment(commentId);
        }
    }


}