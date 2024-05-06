package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.User;
import com.grupor.spoto5.repository.AlbumRepository;
import com.grupor.spoto5.repository.UserRepository;
import com.grupor.spoto5.security.HtmlFilter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityManager;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;


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


    public List<Album> findAll(Integer from, Integer to, String artistName) {

        if (from != null && to != null && from > to) {
            throw new IllegalArgumentException("Initial year must be greater than or equal to Final year");
        }

        String query = "SELECT * FROM album";
        boolean hasWhereClause = false; // Flag to determinate if we had added WHERE clause to the query jet


        if (from != null && to != null) {
            query += " WHERE release_year BETWEEN ? AND ?";
            hasWhereClause = true;
        }

        if (artistName != null && !artistName.isEmpty()) {
            query += hasWhereClause ? " AND artist = ?" : " WHERE artist = ?";
        }

        Query nativeQuery = entityManager.createNativeQuery(query, Album.class);

        int parameterIndex = 1; // Index to set parameters in the query
        if (from != null && to != null) {
            nativeQuery.setParameter(parameterIndex++, from);
            nativeQuery.setParameter(parameterIndex++, to);
        }

        if (artistName != null && !artistName.isEmpty()) {
            nativeQuery.setParameter(parameterIndex, artistName);
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

    public boolean deleteAlbum (long id, boolean isAdmin) throws AccessDeniedException {

        if (isAdmin) {
            try {
                Optional<Album> optionalAlbum = this.findById(id);
                if (optionalAlbum.isPresent()) {
                    Album album = optionalAlbum.get();

                    if (album.getImage() != null && !album.getImage().isEmpty()) {
                        imageService.deleteImage(album.getImage(), isAdmin);
                    }

                    if (album.getImageFile() != null) {
                        album.getImageFile().free();
                    }

                    if (album.getVideoPath() != null && !album.getVideoPath().isEmpty()) {
                        videoService.deleteVideo(album.getVideoPath(), isAdmin);
                    }

                    this.deleteById(id);

                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            throw new AccessDeniedException("Permision Denied");
        }
        return false;
    }


    public void save(Album album, MultipartFile albumImage, MultipartFile albumVideo, Boolean isAdmin) throws IOException {

        if (isAdmin) {
            if (album.getArtist().isEmpty()) {
                throw new IllegalArgumentException("Artist name cannot be empty");
            }

            if (album.getTitle().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty");
            }

            if (album.getRelease_year() < 1000 || album.getRelease_year() > 2024) {
                throw new IllegalArgumentException("Year must be between 1000 and 2024");
            }

            if (album.getText() == null || album.getText().isEmpty()) {
                throw new IllegalArgumentException("Text cannot be empty");
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

            String filteredText = HtmlFilter.filter(album.getText());
            album.setText(filteredText);

            albumRepository.save(album);
        } else {
            throw new AccessDeniedException("Permision Denied");
        }
    }



    public void save (Album album) throws  IOException {

        if (album.getArtist().isEmpty()) {
            throw new IllegalArgumentException("Artist name cannot be empty");
        }

        if (album.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if (album.getText().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be empty");
        }

        if (album.getRelease_year() < 1000 || album.getRelease_year() > 2024) {
            throw new IllegalArgumentException("Year must be between 1000 and 2024");
        }

        albumRepository.save(album);

    }


    public void updateAlbum(Long id, Album updatedAlbum, boolean isAdmin, MultipartFile albumImage, MultipartFile albumVideo) throws IOException {

        if (isAdmin) {

            Optional<Album> existingAlbum = findById(id);

            if (albumImage != null && !albumImage.isEmpty()) {
                String fileImage = imageService.createImage(albumImage);
                updatedAlbum.setImage(fileImage);
                updatedAlbum.setImageFile(BlobProxy.generateProxy(albumImage.getInputStream(), albumImage.getSize()));
            }

            if (albumVideo != null && !albumVideo.isEmpty()) {
                String fileVideo = videoService.createVideo(albumVideo);
                updatedAlbum.setVideoPath(fileVideo);
            }
            if (!existingAlbum.isPresent()) {
                throw new EntityNotFoundException("Album not found");
            }

            if (existingAlbum.isPresent()) {
                Album al = existingAlbum.get();
                if (!al.getArtist().equals(updatedAlbum.getArtist()) && !updatedAlbum.getArtist().isEmpty() && updatedAlbum.getArtist() != null) {
                    al.setArtist(updatedAlbum.getArtist());
                }
                if (!al.getRelease_year().equals(updatedAlbum.getRelease_year()) && updatedAlbum.getRelease_year() != null) {
                    al.setRelease_year(updatedAlbum.getRelease_year());
                }
                if (!al.getTitle().equals(updatedAlbum.getTitle()) && updatedAlbum.getTitle() != null && !updatedAlbum.getTitle().isEmpty()) {
                    al.setTitle(updatedAlbum.getTitle());
                }
                if (!updatedAlbum.getText().equals(al.getText())) {
                    String filteredText = HtmlFilter.filter(updatedAlbum.getText());
                    al.setText(filteredText);
                }

                if (updatedAlbum.getImage() != null && !updatedAlbum.getImage().isEmpty()) {
                    al.setImage(updatedAlbum.getImage());
                }
                if (updatedAlbum.getImageFile() != null) {
                    al.setImageFile(updatedAlbum.getImageFile());
                }

                if (updatedAlbum.getVideoPath() != null && !updatedAlbum.getVideoPath().isEmpty()) {
                    al.setVideoPath(updatedAlbum.getVideoPath());
                }
                if (updatedAlbum.getVideoFile() != null) {
                    al.setVideoFile(updatedAlbum.getVideoFile());
                }

                albumRepository.save(al);
            }
        } else {
            throw new AccessDeniedException("Permision Denied");
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