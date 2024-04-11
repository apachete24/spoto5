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

        if (!originalName.matches(".*\\.(mp4|avi|mov|mkv)")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The file is not a valid video format");
        }

        //String fileName = "video_" + UUID.randomUUID() + "_" + originalName;

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
        try {
            return new UrlResource(videoPath.toUri()); // No es necesario hacer casting
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
