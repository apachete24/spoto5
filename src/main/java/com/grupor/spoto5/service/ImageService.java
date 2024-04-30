package com.grupor.spoto5.service;

import com.grupor.spoto5.model.User;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class ImageService {

    private static final Path IMAGES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");

    public String createImage(MultipartFile multiPartFile) {

        String originalName = multiPartFile.getOriginalFilename();

        if(!originalName.matches(".*\\.(jpg|jpeg|gif|png)")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The url is not an image resource");
        }

        String fileName = "image_" + UUID.randomUUID() + "_" + originalName;

        Path imagePath = IMAGES_FOLDER.resolve(fileName);

        // Verificar que la ruta de la imagen esté dentro del directorio de imágenes
        var pathImage = imagePath.normalize().toAbsolutePath().toString();
        var pathFolder = IMAGES_FOLDER.toAbsolutePath().toString();
        if (!pathImage.startsWith(pathFolder)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image path");
        }

        try {
            multiPartFile.transferTo(imagePath);
        } catch (Exception ex) {
            System.err.println(ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't save image locally", ex);
        }

        return fileName;
    }

    public Resource getImage(String imageName) {
        Path imagePath = IMAGES_FOLDER.resolve(imageName);

        var pathImage = imagePath.normalize().toAbsolutePath().toString();
        var pathFolder = IMAGES_FOLDER.toAbsolutePath().toString();
        if (!pathImage.startsWith(pathFolder)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image path");
        }

        try {
            return new UrlResource(imagePath.toUri());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't get local image");
        }
    }

    /*
    public void deleteImage(String image_url) {
        String[] tokens = image_url.split("/");
        String image_name = tokens[tokens.length -1 ];

        try {
            IMAGES_FOLDER.resolve(image_name).toFile().delete();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't delete local image");
        }
    }
    */

    public void deleteImage(String image_url, Boolean admin) {
        String[] tokens = image_url.split("/");
        String image_name = tokens[tokens.length - 1];

        Path imagePath = IMAGES_FOLDER.resolve(image_name);

        // Verificar que el usuario actual esté autorizado para eliminar el archivo
        if (!admin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }

        // Verificar que el archivo exista
        if (!Files.exists(imagePath)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
        }

        try {
            // Eliminar el archivo
            Files.delete(imagePath);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't delete local image", e);
        }
    }

}




