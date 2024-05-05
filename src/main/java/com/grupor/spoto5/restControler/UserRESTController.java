package com.grupor.spoto5.restControler;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.grupor.spoto5.model.*;
import com.grupor.spoto5.service.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
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


    // OPERATIONS RESTRICTED TO ADMIN ROLE

    // Get all users
    @GetMapping("")
    public ResponseEntity<Collection<User>> getAllUsers(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        if (principal == null){ // not user logged
            return ResponseEntity.notFound().build();

        } else {
            // If the user is an admin, return all users
            User user = userService.findByName(principal.getName()).get();
            if(user.getRoles().contains("ADMIN")){
                return ResponseEntity.ok(userService.findAll());
            }
        }
        // In any other case, return not found
        return ResponseEntity.notFound().build();
    }



    // Get current user
    @GetMapping("/me")
    public ResponseEntity<User> me(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if(principal != null) {
            return ResponseEntity.ok(userService.findByName(principal.getName()).orElseThrow());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Get user by id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id, Model model) {
            // we need to change this code: check the id is equal to the current user id
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