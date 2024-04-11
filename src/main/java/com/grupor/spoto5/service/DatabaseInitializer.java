package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.Comment;
import com.grupor.spoto5.repository.AlbumRepository;
import com.grupor.spoto5.repository.CommentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

@Component
public class DatabaseInitializer {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private VideoService videoService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

    @PostConstruct
    public void init() throws IOException, SQLException {

        Album album1 = new Album("Nico Miseria", "Tercer Verano del Amor", 2021, "Una fotografía visceral de un momento vital concreto, así definiría Tercer Verano del Amor. El nuevo disco de Nico Miseria es una experiencia musical absoluta, hipnótica, un carrusel de emociones que atrapa al oyente de principio a fin sin posibilidad alguna de dejarlo indiferente.");
        album1.setImage("Tercer Verano del Amor.jpg");
        album1.setImageFile(createBlobFromFile(new File("images/Tercer Verano del Amor.jpg")));
        album1.setVideoPath("Nico Miseria - PRELUDIO A LA SIESTA DE UN FAUNO (Tercer Verano del Amor).mp4");

        Album album2 = new Album("Cruz Cafune", "Me muevo con dios", 2023, "En este álbum, Cruzzi se consolida como uno de los artistas más respetados y queridos de nuestra escena. Primero, por el nivel de sus letras, siempre a la altura y conservando la esencia. Segundo, porque las producciones del álbum y los samples usados son simplemente espectaculares. Y tercero, pero no menos importante, por un tracklist lleno de colaboraciones impecables, que van desde Westside Gunn a Miky Woodz pasando por Leïti, LaBlackie o Hoke, por nombrar solo algunos. Es para muchos ya un álbum no skips, o sea, que todos los temas son imprescindibles y no hay ni uno que quieras saltarte.");
        album2.setImage("Me muevo con Dios.jpg");
        album2.setImageFile(createBlobFromFile(new File("images/Me muevo con Dios.jpg")));
        album2.setVideoPath("CRUZ CAFUNÉ - Cangrinaje (Visualizer).mp4");

        Album album3 = new Album("Hoke", "BBO", 2022, "El disco de Hoke y Louis Amoeba tiene doble mérito. Primero porque es buenísimo, superior, Olímpico. Segundo, porque ha conseguido que todo el mundo se deshaga de la pesada soga del ego y reconozca que, efectivamente, lo es. BBO nos ha puesto a todos de acuerdo. Y seamos realistas, ¿hacía cuánto que un disco no conseguía algo así? En un ecosistema tan polarizado como es el rap español, el trabajo del dúo valenciano ha funcionado como un imán, un agujero negro que ha conseguido atraer miradas y halagos.");
        album3.setImage("BBO.jpg");
        album3.setImageFile(createBlobFromFile(new File("images/BBO.jpg")));
        album3.setVideoPath("HOKE - MEDALLONES (Prod. Louis Amoeba).mp4");

        Album album4 = new Album("Ill Pekeño", "Av. Rafaela Ybarra", 2021, "Hablamos probablemente de una de las tapes más serias del rap español en años. Ill Pequeño, para quien no conozca su nombre, tiene seis cortes de auténticas barras en este trabajo que servirán de presentación. En compañía del inseparable Ergo Pro, era hora de que viéramos un trabajo por uno de los raperos que, siendo underground hasta la médula, es uno de los MCs mas fuertes de la escena madrileña y probablemente española. Esto es Av. Rafaela Ybarra:");
        album4.setImage("Av Rafaela Ybarra.jpg");
        album4.setImageFile(createBlobFromFile(new File("images/Av Rafaela Ybarra.jpg")));
        album4.setVideoPath("Calle Cortada.mp4");

        Album album5 = new Album("Yung Beef", "ADROMICFS 4", 2018, "Es un viaje solitario (su primer disco sin feats, y también el más extenso) cuyo territorio es la noche, como en 'Me perdí en Madrid', producida por la dupla Lowlight, donde Yung Beef va 'por la avenida con ideas suicidas'. Su visión punk del trap está por todas partes. Sin embargo, lo que define al disco es el conocimiento que Yung Beef tiene de las profundidades de la pista de baile.");
        album5.setImage("ADROMICFMS4.jpg");
        album5.setImageFile(createBlobFromFile(new File("images/ADROMICFMS4.jpg")));
        album5.setVideoPath("YUNG BEEF-INFIERNO.mp4");

        Album album6 = new Album("Diego 900", "La Espalda del Sol", 2023, "El álbum cuenta con 18 temas los cuáles forman un tracklist en el que hay una muy interesante variedad tanto de estilos como de artistas y productores invitados. Diego se mueve entre una infinidad de géneros que van desde el R&B, el rap, el dancehall, los ritmos latinos y el hip hop. Finalmente, este álbum de 'amor, desamor y sensualidad' cumple con las expectativas de uno de los discos más esperados del año del panorama del R&B y rap español.");
        album6.setImage("La espalda del sol.jpg");
        album6.setImageFile(createBlobFromFile(new File("images/La espalda del sol.jpg")));
        album6.setVideoPath("1. DIEGO 900 - T4 BARAJAS.mp4");

        Album album7 = new Album("Gloosito", "Psipocompo", 2021, "Siempre que presenta material se hace notar. Una audiencia fiel, de esa que muestra un apoyo fiel y se sumerge en cada propuesta como si fuera la última, ha hecho que Gloosito esté posicionado como uno de los artistas del año. El miembro de CTDS aprovecha su low profile para seguir haciendo lo que le nace, experimentando y poniendo los cimientos sobre un sonido y delivery único en España.");
        album7.setImage("Psipocompo.jpg");
        album7.setImageFile(createBlobFromFile(new File("images/Psipocompo.jpg")));
        album7.setVideoPath("Gloosito - No competition.mp4");

        Album album8 = new Album("Dellafuente", "Azulejos de Corales", 2015, "Con esta fórmula el granadino Dellafuente ha logrado un importante número de seguidores en las redes sociales, los suficientes como para lanzarse a grabar su primer álbum, 'Azulejos de colores', un disco de dieciocho canciones en las que la cultura flamenca se casa con otras temáticas como la competición, el amor/sexo o la vida en la calle, bajo un lenguaje y unos códigos (aquí sí) propios del rap. Una fusión que no es nueva pero que cada vez parece tener más éxito. Y en la que Dellafuente navega con mucha frescura, más aún tratándose de su disco debut.");
        album8.setImage("azulejos de corales.jpg");
        album8.setImageFile(createBlobFromFile(new File("images/azulejos de corales.jpg")));
        album8.setVideoPath("No Vendo Humo.mp4");

        Album album9 = new Album("Kaydy Cain", "3 Sentimientos", 2009, "Como todos bien sabéis, su carrera se inició como D. Gómez siendo miembro de uno de los colectivos más representativos del panorama como Corredores de Bloque, ahora convertido en Takers. El artista madrileño afincado en Barcelona, ha decidido publicar en su cuenta de Bandcamp toda su discografía en solitario, desde sus inicios hasta la citada anteriormente con Cookin Soul. En resumen, podemos encontrar desde '3 Sentimientos', de donde rescataron hace no mucho el remix de 'El infierno de tu gloria' con Marko Italia, hasta clásicos como 'Trvp Jinxx' publicada en 2003 o 'Música pa vacilar' de 2012, pasando por el resto de trabajos que han hecho que Kaydy Cain aka D. Gómez sea uno de los artistas más activos a nivel nacional.");
        album9.setImage("3 Sentimientos.jpg");
        album9.setImageFile(createBlobFromFile(new File("images/3 Sentimientos.jpg")));
        album9.setVideoPath("El Infierno de Tu Gloria.mp4");

        Album album10 = new Album("Test", "Test", 2024, "aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        album10.setImage("no-image.png");
        album10.setImageFile(createBlobFromFile(new File("images/no-image.png")));

        albumService.save(album1);
        albumService.save(album2);
        albumService.save(album3);
        albumService.save(album4);
        albumService.save(album5);
        albumService.save(album6);
        albumService.save(album7);
        albumService.save(album8);
        albumService.save(album9);
        albumService.save(album10);

        Comment comment1 = new Comment("Rodri", 100, "Great Album");
        comment1.setAlbum(album1);
        commentService.addComment(comment1);

        Comment comment2 = new Comment("Diego", 90, "Niceeee");
        comment2.setAlbum(album1);
        commentService.addComment(comment2);

        Comment comment3 = new Comment("Mario", 95, "Sounds good");
        comment3.setAlbum(album1);
        commentService.addComment(comment3);

        Comment comment4 = new Comment("Rodri", 100, "922 928");
        comment4.setAlbum(album2);
        commentService.addComment(comment4);

        Comment comment5 = new Comment("Diego", 100, "Fantastic");
        comment5.setAlbum(album2);
        commentService.addComment(comment5);

        Comment comment6 = new Comment("Mario", 100, "Album of the year");
        comment6.setAlbum(album2);
        commentService.addComment(comment6);

        Comment comment7 = new Comment("Rodri", 100, "Timeless");
        comment7.setAlbum(album5);
        commentService.addComment(comment7);

        Comment comment8 = new Comment("Diego", 70, "Nice Album");
        comment8.setAlbum(album8);
        commentService.addComment(comment8);

        Comment comment9 = new Comment("Mario", 100, "Great Album");
        comment9.setAlbum(album8);
        commentService.addComment(comment9);

        Comment comment10 = new Comment("Rodri", 100, "Great Album");
        comment10.setAlbum(album9);
        commentService.addComment(comment10);

        Comment comment11 = new Comment("Diego", 100, "Great Album");
        comment11.setAlbum(album4);
        commentService.addComment(comment11);

        Comment comment12 = new Comment("Mario", 100, "Rookie");
        comment12.setAlbum(album3);
        commentService.addComment(comment12);

    }

    public static Blob createBlobFromFile(File file) throws IOException, SQLException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] bytes = new byte[(int) file.length()];
            inputStream.read(bytes);
            return new SerialBlob(bytes);
        }
    }

}
