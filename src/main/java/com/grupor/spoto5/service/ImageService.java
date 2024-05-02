package com.grupor.spoto5.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    private static final Path IMAGES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "gif", "png");

    public String createImage(MultipartFile multiPartFile) {
        String originalName = multiPartFile.getOriginalFilename();

        if (!isValidImageFile(originalName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The URL is not an image resource");
        }

        String fileName = generateUniqueFileName(originalName);

        try {
            Path imagePath = IMAGES_FOLDER.resolve(fileName);
            multiPartFile.transferTo(imagePath);
            return fileName;
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't save image locally", ex);
        }
    }

    public Resource getImage(String imageName) {
        Path imagePath = IMAGES_FOLDER.resolve(imageName);
        verifyImagePath(imagePath);

        try {
            return new UrlResource(imagePath.toUri());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't get local image", e);
        }
    }

    public void deleteImage(String imageUrl, Boolean admin) {
        String imageName = StringUtils.getFilename(imageUrl);

        if (!admin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }

        Path imagePath = IMAGES_FOLDER.resolve(imageName);
        verifyImagePath(imagePath);

        if (!Files.exists(imagePath)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
        }

        try {
            Files.delete(imagePath);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't delete local image", e);
        }
    }

    private void verifyImagePath(Path imagePath) {
        var pathImage = imagePath.normalize().toAbsolutePath().toString();
        var pathFolder = IMAGES_FOLDER.toAbsolutePath().toString();
        if (!pathImage.startsWith(pathFolder)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image path");
        }
    }

    private boolean isValidImageFile(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return false;
        }
        String extension = StringUtils.getFilenameExtension(fileName);
        return StringUtils.hasText(extension) && ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
    }

    private String generateUniqueFileName(String originalName) {
        String fileName = "image_" + UUID.randomUUID() + "_" + originalName;
        // Ensure file name is unique in the images folder
        while (Files.exists(IMAGES_FOLDER.resolve(fileName))) {
            fileName = "image_" + UUID.randomUUID() + "_" + originalName;
        }
        return fileName;
    }
}
