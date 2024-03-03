package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.Comment;
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
        // Inicialización con algunos álbumes de ejemplo
        save(new Album("Nico Miseria", "Tercer Verano del Amor", 2022,  "Una fotografía visceral de un momento vital concreto, así definiría Tercer Verano del Amor. \nEl nuevo disco de Nico Miseria es una experiencia musical absoluta, hipnótica, un carrusel de emociones que atrapa al oyente de principio a fin sin posibilidad alguna de dejarlo indiferente."));
        save(new Album("Cruz Cafune", "Me muevo con dios", 2023, "En este álbum, Cruzzi se consolida como uno de los artistas más respetados y queridos de nuestra escena. \nPrimero, por el nivel de sus letras, siempre a la altura y conservando la esencia. Segundo, porque las producciones del álbum y los samples usados son simplemente espectaculares. \nY tercero, pero no menos importante, por un tracklist lleno de colaboraciones impecables, que van desde Westside Gunn a Miky Woodz pasando por Leïti, LaBlackie o Hoke, por nombrar solo algunos. \nEs para muchos ya un álbum no skips, o sea, que todos los temas son imprescindibles y no hay ni uno que quieras saltarte."));
        save(new Album("Hoke", "BBO", 2021, "El disco de Hoke y Louis Amoeba tiene doble mérito. Primero porque es buenísimo, superior, Olímpico. Segundo, porque ha conseguido que todo el mundo se deshaga de la pesada soga del ego y reconozca que, efectivamente, lo es. \nBBO nos ha puesto a todos de acuerdo. Y seamos realistas, ¿hacía cuánto que un disco no conseguía algo así? En un ecosistema tan polarizado como es el rap español, el trabajo del dúo valenciano ha funcionado como un imán, un agujero negro que ha conseguido atraer miradas y halagos."));
    }

    public Collection<Album> findAll() {
        return albums.values();
    }

    public Album findById(long id) {
        return albums.get(id);
    }

    public void deleteById(long id) {
        albums.remove(id);
    }

    public void save(Album album) {
        long id = nextId.getAndIncrement();
        album.setId(id);
        albums.put(id, album);
    }

    public void addComment(long albumId, Comment comment) {
        Album album = findById(albumId);
        if (album != null) {
            long commentId = nextId.getAndIncrement();
            comment.setId(commentId);
            album.addComment(comment);
        }
    }

    public void deleteComment(long albumId, long commentId) {
        Album album = findById(albumId);
        if (album != null) {
            album.deleteComment(commentId);
        }
    }


}