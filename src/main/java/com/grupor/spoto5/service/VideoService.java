package com.grupor.spoto5.service;


import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.core.io.Resource;


@Service
public class VideoService {

    private static final Path VIDEOS_FOLDER = Paths.get(System.getProperty("user.dir"), "videos");

    public String createVideo(MultipartFile multiPartFile) {
        String originalName = multiPartFile.getOriginalFilename();

        // Validación del nombre del archivo y su extensión
        if (!originalName.matches(".*\\.(mp4|avi|mov|mkv)")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The file is not a valid video format");
        }

        Path videoPath = VIDEOS_FOLDER.resolve(originalName);

        try {
            multiPartFile.transferTo(videoPath);
        } catch (Exception ex) {
            System.err.println(ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't save video locally", ex);
        }

        return originalName;
    }

    public Resource getVideo(String videoName) {
        Path videoPath = VIDEOS_FOLDER.resolve(videoName);

        // Restricción del acceso a archivos dentro del directorio permitido
        if (!videoPath.startsWith(VIDEOS_FOLDER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access to the requested video is forbidden");
        }

        try {
            return new UrlResource(videoPath.toUri());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't get local video");
        }
    }

    public void deleteVideo(String videoName) {
        try {
            VIDEOS_FOLDER.resolve(videoName).toFile().delete();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't delete local video");
        }
    }
}
