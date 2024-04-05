package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.Comment;
import com.grupor.spoto5.repository.AlbumRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ImageService imageService;

    public Collection<Album> findAll() {
        // return albums.values();
        return albumRepository.findAll();
    }


    public Optional<Album> findById (long id) {
        return albumRepository.findById(id);
    }


    public void deleteById (long id) {
        this.albumRepository.deleteById(id);
    }


    public void save(Album album, MultipartFile image) {

        if (image != null && !image.isEmpty()) {
            String path = this.imageService.createImage(image);
            album.setImage(path);
        } else {
            // Set a default image path if no image uploaded
            album.setImage("no-image.png");
        }

        albumRepository.save(album);
    }

    public void updateAlbum (Album updatedAlbum) {
        Album album = albumRepository.findById(updatedAlbum.getId()).orElseThrow();
        album.getComments().forEach(updatedAlbum::addComment);
        albumRepository.save(updatedAlbum);
    }
}