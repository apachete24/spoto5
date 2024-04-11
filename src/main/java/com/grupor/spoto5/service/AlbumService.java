package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.User;
import com.grupor.spoto5.repository.AlbumRepository;
import com.grupor.spoto5.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityManager;
import org.springframework.util.StringUtils;
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

    @Autowired
    private VideoService videoService;



    public List<Album> findAll(Integer from, Integer to) {

        if (from != null && to != null && from > to) {
            throw new IllegalArgumentException("Initial year must be greater than or equal to Final year");
        }

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
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid album id: " + id);
        }
        return albumRepository.findById(id);
    }


    public void deleteById (long id) {
        this.albumRepository.deleteById(id);
    }



    public void save(Album album, MultipartFile albumImage, MultipartFile albumVideo) throws IOException {

        if (album.getArtist().isEmpty()) {
            throw new IllegalArgumentException("Artist name cannot be empty");
        }

        if (album.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if (album.getRelease_year() < 1000 || album.getRelease_year() > 2024) {
            throw new IllegalArgumentException("Year must be between 1000 and 2024");
        }

        if (albumImage != null && !albumImage.isEmpty()) {
            String fileImage = imageService.createImage(albumImage);
            if (!StringUtils.hasText(fileImage) || !isImageFormatValid(fileImage)) {
                throw new IllegalArgumentException("Invalid image format");
            }
            album.setImageFile(BlobProxy.generateProxy(albumImage.getInputStream(), albumImage.getSize()));
            album.setImage(fileImage);
        }

        if (albumVideo != null && !albumVideo.isEmpty()) {
            String fileVideo = videoService.createVideo(albumVideo);
            album.setVideoPath(fileVideo);
        }
        albumRepository.save(album);
    }


    public void save (Album album) throws  IOException {
        if (album.getArtist().isEmpty()) {
            throw new IllegalArgumentException("Artist name cannot be empty");
        }

        if (album.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if (album.getRelease_year() < 1000 || album.getRelease_year() > 2024) {
            throw new IllegalArgumentException("Year must be between 1000 and 2024");
        }

        albumRepository.save(album);
    }

    public void updateAlbum(Long id, Album updatedAlbum) {

        Optional<Album> existingAlbum = findById(id);

        if (!existingAlbum.isPresent()) {
            throw new EntityNotFoundException("Album not found");
        }

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

            if (updatedAlbum.getVideoPath() != null && !updatedAlbum.getVideoPath().isEmpty()) {
                al.setVideoPath(updatedAlbum.getVideoPath());
            } if (updatedAlbum.getVideoFile() != null) {
                al.setVideoFile(updatedAlbum.getVideoFile());
            }

            albumRepository.save(al);

        }
    }

    public void addAlbumToFavorites(User user, Album album) {
        user.getAlbumFavs().add(album);
        album.getUserFavs().add(user);
        albumRepository.save(album);
    }

    public void removeAlbumFromFavorites(User user, Album album) {
        user.getAlbumFavs().remove(album);
        album.getUserFavs().remove(user);
        albumRepository.save(album);
    }

    public void addUsersToFavorites(Long albumId, List<Long> userIds) {
        Optional<Album> albumOptional = albumRepository.findById(albumId);
        if (albumOptional.isPresent()) {
            Album album = albumOptional.get();
            List<User> users = userRepository.findAllById(userIds);
            for (User user : users) {
                user.getAlbumFavs().add(album);
            }
            userRepository.saveAll(users);
        }
    }

    private boolean isImageFormatValid(String fileName) {
        String extension = StringUtils.getFilenameExtension(fileName);
        if (extension != null) {
            String[] allowedExtensions = {"jpg", "jpeg", "gif", "png"};
            for (String ext : allowedExtensions) {
                if (ext.equalsIgnoreCase(extension)) {
                    return true;
                }
            }
        }
        return false;
    }

}