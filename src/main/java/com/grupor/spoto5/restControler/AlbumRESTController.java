package com.grupor.spoto5.restControler;
import  com.grupor.spoto5.service.AlbumService;
import  com.grupor.spoto5.model.Album;

import com.grupor.spoto5.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class AlbumRESTController{


    @Autowired
    private AlbumService albums;
    @Autowired
    private ImageService images;


    @GetMapping("/albums")
    public Collection<Album> getAlbums() {
        return albums.findAll();

    }



}
