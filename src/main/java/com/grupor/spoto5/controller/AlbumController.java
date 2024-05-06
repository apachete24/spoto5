package com.grupor.spoto5.controller;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.User;
import com.grupor.spoto5.repository.UserRepository;
import com.grupor.spoto5.service.*;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Controller
public class AlbumController {

    private static final String ALBUMS_FOLDER = "albums";

    @Autowired
    private ImageService imageService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private UserRepository userRepository;


    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {

            model.addAttribute("logged", true);
            model.addAttribute("currentUser", principal.getName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
            User user = userRepository.findByName(principal.getName()).orElseThrow();
            model.addAttribute("user", user);

        } else {
            model.addAttribute("logged", false);
        }
    }

    // Get all albums if no parameters are passed, or get albums between from and to years.
    @GetMapping("/")
    public String showAlbums(Model model, @RequestParam(required = false) Integer from, @RequestParam(required = false) Integer to, @RequestParam(required = false) String artistName) {

        try {
            List<Album> albums = albumService.findAll(from, to, artistName);
            model.addAttribute("albums", albums);
        } catch (IllegalArgumentException ex) {
            String errorMessage = "Invalid range: " + ex.getMessage();
            model.addAttribute("errorMessage", errorMessage);
        }
        return "index";
    }

    // Get album by id
    @GetMapping("/album/{id}")
    public String showAlbum(Model model, @PathVariable long id) {
        try {
            Optional<Album> album = albumService.findById(id);
            if (album.isPresent()) {
                model.addAttribute("album", album.get());
                model.addAttribute("comments", commentService.getComments(id));
                Collection<User> users = userService.findAll();
                model.addAttribute("users", users);
                return "show_album";
            } else {
                return "error";
            }
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "error";
        }
    }


    @GetMapping("/newalbum")
    @PreAuthorize("hasRole('ADMIN')")
    public String newAlbumForm(Model model) {
        boolean isAdmin = (boolean) model.getAttribute("admin");
        if (!isAdmin) {
            return "denied";
        }
        return "new_album";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/newalbum")
    public String newAlbum(Model model, Album album, @RequestParam MultipartFile albumImage, @RequestParam(required = false) MultipartFile albumVideo) throws IOException {
        boolean isAdmin = (boolean) model.getAttribute("admin");
        if (!isAdmin) {
            return "denied";
        }
        try {
            albumService.save(album, albumImage, albumVideo, isAdmin);
            return "saved_album";
        } catch (AccessDeniedException ex) {
            return "denied";
        }
    }


    @GetMapping("/album/{id}/image")
    public ResponseEntity<Object> downloadImage(Model model, @PathVariable int id) throws SQLException {

        Optional<Album> al = albumService.findById(id);

        if (al.isPresent()) {
            Album album = al.get();
            Resource poster = imageService.getImage(album.getImage());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpg").body(poster);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found");
        }
    }


    @GetMapping("/album/{id}/video")
    public ResponseEntity<Resource> downloadVideo(Model model, @PathVariable long id) {
        Optional<Album> albumOptional = albumService.findById(id);

        if (albumOptional.isPresent()) {
            Album album = albumOptional.get();

            if (album.getVideoPath() != null && !album.getVideoPath().isEmpty()) {
                Resource video = (Resource) videoService.getVideo(album.getVideoPath());

                if (video != null) {
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                            .body(video);
                }
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/deletealbum/{id}")
    public String deleteAlbum(Model model, @PathVariable long id) {

        boolean isAdmin = (boolean) model.getAttribute("admin");

        if (!isAdmin) {
            return "denied";
        }

        try {
            albumService.deleteAlbum(id, isAdmin);

            return "deleted_album";
        } catch (AccessDeniedException ex) {
            return "denied";
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/editalbum/{id}")
    public String updateAlbum(Model model, @PathVariable long id) {

        boolean isAdmin = (boolean) model.getAttribute("admin");

        if (!isAdmin) {
            return "denied";
        }

        Optional<Album> optionalAlbum = albumService.findById(id);

        if (optionalAlbum.isPresent()) {

            Album album = optionalAlbum.get();
            model.addAttribute("album", album);

            model.addAttribute("imageFileName", album.getImage());

            return "edit_album";
        } else {
            return "error";
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/editalbum/{id}")
    public String updateAlbum(Model model, @PathVariable Long id, Album updatedAlbum, @RequestParam(required = false) MultipartFile albumImage, @RequestParam(required = false) MultipartFile albumVideo) throws IOException {

        boolean isAdmin = (boolean) model.getAttribute("admin");

        if (!isAdmin) {
            return "denied";
        }

        try {
            albumService.updateAlbum(id, updatedAlbum, isAdmin, albumImage, albumVideo);
            return "redirect:/album/" + id;
        } catch (AccessDeniedException exception) {
            return "denied";
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/album/{id}/like")
    public String likeAlbum(HttpServletRequest request, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Verificar si el usuario está autenticado
        if (request.getUserPrincipal() == null) {
            // Si el usuario no está autenticado, redirigir a la página de error
            return "redirect:/error";
        }

        Optional<Album> albumOptional = albumService.findById(id);
        if (albumOptional.isPresent()) {
            Album album = albumOptional.get();
            Optional<User> userOptional = userService.findByName(request.getUserPrincipal().getName());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                // Verificar si el usuario ya ha marcado este álbum como favorito
                if (user.getAlbumFavs().contains(album)) {
                    // Si ya lo ha marcado, eliminarlo de la lista de favoritos
                    userService.removeUserFromAlbumFavorites(user, album);
                } else {
                    // Si no lo ha marcado, agregarlo a la lista de favoritos
                    userService.addUserToAlbumFavorites(user, album);
                }
            }
            redirectAttributes.addFlashAttribute("message", "Action performed successfully");
            return "redirect:/album/" + id;
        } else {
            // Manejar el error cuando el álbum no se encuentra
            return "redirect:/error";
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users")
    public String showUsers(Model model) {
        Collection<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users"; // Aquí debes tener una vista llamada "users.html" para mostrar los usuarios como botones
    }


    @GetMapping("/favorites")
    public String showUserFavoriteAlbums(Model model) {
        Optional<User> userOptional = userService.findByName((String) model.getAttribute("currentUser"));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Album> favoriteAlbums = user.getAlbumFavs();
            model.addAttribute("user", user.getName());
            model.addAttribute("favoriteAlbums", favoriteAlbums);
            return "user_favorite_albums";
        } else {
            // Manejar el caso en el que el usuario no existe
            return "error";
        }

    }
}
