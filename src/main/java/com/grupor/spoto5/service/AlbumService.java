package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AlbumService {

    private ConcurrentMap<Long, Album> albums = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong();

    public AlbumService() {
        save(new Album("Nico Miseria", "Tercer Verano del Amor", 2019,  "aaaaaaaaaa"));
        save(new Album("Cruz Cafuné", "Me Muevo con Dios", 2023,  "goated"));
    }

    public Collection<Album> findAll() {
        return albums.values();
    }

    public Album findById (long id) {
        return albums.get(id);
    }

    public void deleteById (long id) {
        this.albums.remove(id);
    }
    public void save(Album album) {

        long id = nextId.getAndIncrement();

        album.setId(id);

        this.albums.put(id, album);
    }



}