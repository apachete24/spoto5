package com.grupor.spoto5.service;


import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;


@Service
public class VideoService {



    private static final Path VIDEOS_FOLDER = Paths.get(System.getProperty("user.dir"), "videos");
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("mp4", "avi", "mov", "mkv");


    public String createVideo(MultipartFile multiPartFile) {
        String originalName = multiPartFile.getOriginalFilename();

        // Validación del nombre del archivo y su extensión
        if (!isValidVideoFile(originalName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The file is not a valid video format");
        }

        Path videoPath = VIDEOS_FOLDER.resolve(originalName);
        verifyVideoPath(videoPath);

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
        verifyVideoPath(videoPath);
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



    public void deleteVideo(String videoName, Boolean admin) {

        if (!admin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }

        Path videoPath = VIDEOS_FOLDER.resolve(videoName);
        verifyVideoPath(videoPath);

        if (!Files.exists(videoPath)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found");
        }

        try {
            Files.delete(videoPath);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't delete local video", e);
        }
    }


    private boolean isValidVideoFile(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return false;
        }
        String extension = StringUtils.getFilenameExtension(fileName);
        return StringUtils.hasText(extension) && ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
    }



    private void verifyVideoPath(Path videoPath) {
        var pathVideo = videoPath.normalize().toAbsolutePath().toString();
        var pathFolder = VIDEOS_FOLDER.toAbsolutePath().toString();
        if (!pathVideo.startsWith(pathFolder)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid video path");
        }
    }


}
