package com.grupor.spoto5.restControler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grupor.spoto5.model.*;
import com.grupor.spoto5.service.*;

import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UserRESTController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AlbumService albumService;

    // Get all users (Restricted to ADMIN role)
    @GetMapping("")
    public Collection<User> getAllUsers() {
        return userService.findAll();
    }

    // Get user by id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Get user albums favorites
    @GetMapping("/{id}/albums")
    public Collection<Album> getUserAlbums(@PathVariable long id) {

        return userService.getUserAlbums(id);
    }



    // Add user album favorite
    @PostMapping("/{id}/albums")
    public ResponseEntity<Album> addUserAlbum(@PathVariable long id, @RequestParam long albumId) {

        // If the user or the album does not exist, return not found
        Optional<User> user = userService.findById(id);
        Optional<Album> album = albumService.findById(albumId);
        if (!user.isPresent() || (!album.isPresent())) {

            return ResponseEntity.notFound().build();

        }else{

            userService.addUserToAlbumFavorites(user.get(), album.get());
            return ResponseEntity.ok(album.get());
        }


    }

    // Remove user album favorite
    @DeleteMapping("/{id}/albums")
    public ResponseEntity<Album> removeUserAlbum(@PathVariable long id, @RequestParam long albumId) {

        // If the user or the album does not exist, return not found
        Optional<User> user = userService.findById(id);
        Optional<Album> album = albumService.findById(albumId);
        if (!user.isPresent() || (!album.isPresent())) {

            return ResponseEntity.notFound().build();

        }else{

            userService.removeUserFromAlbumFavorites(user.get(), album.get());
            return ResponseEntity.ok().build();
        }


    }






}