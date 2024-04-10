package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.repository.AlbumRepository;
import com.grupor.spoto5.repository.UserRepository;
import jakarta.persistence.Query;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityManager;
import org.springframework.web.multipart.MultipartFile;


@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;


    public List<Album> findAll(Integer from, Integer to) {
        String query = "SELECT * FROM album";
        if (from != null && to != null) {
            query += " WHERE release_year BETWEEN :fromYear AND :toYear";
        }

        Query nativeQuery = entityManager.createNativeQuery(query, Album.class);
        if (from != null && to != null) {
            nativeQuery.setParameter("fromYear", from);
            nativeQuery.setParameter("toYear", to);
        }

        return (List<Album>) nativeQuery.getResultList();
    }


    public Optional<Album> findById (long id) {
        return albumRepository.findById(id);
    }


    public void deleteById (long id) {
        this.albumRepository.deleteById(id);
    }



    public void save (Album album, MultipartFile albumImage) throws IOException {
        album.setImageFile(BlobProxy.generateProxy(albumImage.getInputStream(), albumImage.getSize()));
        String file = imageService.createImage(albumImage);
        album.setImage(file);
        albumRepository.save(album);
    }

    public void save (Album album) throws  IOException {
        albumRepository.save(album);
    }

    public void updateAlbum(Long id, Album updatedAlbum) {

        Optional<Album> existingAlbum = findById(id);

        if (existingAlbum.isPresent()) {
            Album al = existingAlbum.get();
            if (! al.getArtist().equals(updatedAlbum.getArtist())) {
                al.setArtist(updatedAlbum.getArtist());
            } if (! al.getRelease_year().equals(updatedAlbum.getRelease_year())) {
                al.setRelease_year(updatedAlbum.getRelease_year());
            } if (! al.getTitle().equals(updatedAlbum.getTitle())) {
                al.setTitle(updatedAlbum.getTitle());
            } if (! al.getText().equals(updatedAlbum.getText())) {
                al.setText(updatedAlbum.getText());
            }

            if (updatedAlbum.getImage() != null && !updatedAlbum.getImage().isEmpty()) {
                al.setImage(updatedAlbum.getImage());
            } if (updatedAlbum.getImageFile() != null) {
                al.setImageFile(updatedAlbum.getImageFile());
            }

            albumRepository.save(al);

        }
    }

}