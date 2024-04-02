package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    @Autowired
    private AlbumService albumService;

    @PostConstruct
    public void init() {
        // Initialize with some albums
        Album album1 = new Album("Nico Miseria", "Tercer Verano del Amor", 2021,  "Una fotografía visceral de un momento vital concreto, así definiría Tercer Verano del Amor. El nuevo disco de Nico Miseria es una experiencia musical absoluta, hipnótica, un carrusel de emociones que atrapa al oyente de principio a fin sin posibilidad alguna de dejarlo indiferente.");
        album1.setImage("Tercer Verano del Amor.jpg");
        Album album2 = new Album("Cruz Cafune", "Me muevo con dios", 2023, "En este álbum, Cruzzi se consolida como uno de los artistas más respetados y queridos de nuestra escena. Primero, por el nivel de sus letras, siempre a la altura y conservando la esencia. Segundo, porque las producciones del álbum y los samples usados son simplemente espectaculares. Y tercero, pero no menos importante, por un tracklist lleno de colaboraciones impecables, que van desde Westside Gunn a Miky Woodz pasando por Leïti, LaBlackie o Hoke, por nombrar solo algunos. Es para muchos ya un álbum no skips, o sea, que todos los temas son imprescindibles y no hay ni uno que quieras saltarte.");
        album2.setImage("Me muevo con Dios.jpg");
        Album album3 = new Album("Hoke", "BBO", 2022, "El disco de Hoke y Louis Amoeba tiene doble mérito. Primero porque es buenísimo, superior, Olímpico. Segundo, porque ha conseguido que todo el mundo se deshaga de la pesada soga del ego y reconozca que, efectivamente, lo es. BBO nos ha puesto a todos de acuerdo. Y seamos realistas, ¿hacía cuánto que un disco no conseguía algo así? En un ecosistema tan polarizado como es el rap español, el trabajo del dúo valenciano ha funcionado como un imán, un agujero negro que ha conseguido atraer miradas y halagos.");
        album3.setImage("BBO.jpg");
        Album album4 = new Album("Ill Pekeño", "Av. Rafaela Ybarra", 2021, "Hablamos probablemente de una de las tapes más serias del rap español en años. Ill Pequeño, para quien no conozca su nombre, tiene seis cortes de auténticas barras en este trabajo que servirán de presentación. En compañía del inseparable Ergo Pro, era hora de que viéramos un trabajo por uno de los raperos que, siendo underground hasta la médula, es uno de los MCs mas fuertes de la escena madrileña y probablemente española. Esto es Av. Rafaela Ybarra:");
        album4.setImage("Av Rafaela Ybarra.jpg");
        Album album5 = new Album("Yung Beef", "ADROMICFS 4", 2018, "Es un viaje solitario (su primer disco sin feats, y también el más extenso) cuyo territorio es la noche, como en 'Me perdí en Madrid', producida por la dupla Lowlight, donde Yung Beef va 'por la avenida con ideas suicidas'. Su visión punk del trap está por todas partes. Sin embargo, lo que define al disco es el conocimiento que Yung Beef tiene de las profundidades de la pista de baile.");
        album5.setImage("ADROMICFMS4.jpg");
        Album album6 = new Album("Diego 900", "La Espalda del Sol", 2023, "El álbum cuenta con 18 temas los cuáles forman un tracklist en el que hay una muy interesante variedad tanto de estilos como de artistas y productores invitados. Diego se mueve entre una infinidad de géneros que van desde el R&B, el rap, el dancehall, los ritmos latinos y el hip hop. Finalmente, este álbum de 'amor, desamor y sensualidad' cumple con las expectativas de uno de los discos más esperados del año del panorama del R&B y rap español.");
        album6.setImage("La espalda del sol.jpg");

        albumService.save(album1, null);
        albumService.save(album2, null);
        albumService.save(album3, null);
        albumService.save(album4, null);
        albumService.save(album5, null);
        albumService.save(album6, null);
    }

}
