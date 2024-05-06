package com.grupor.spoto5.restControler;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.grupor.spoto5.model.*;
import com.grupor.spoto5.service.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.grupor.spoto5.security.jwt.LoginRequest;

@RestController
@RequestMapping("/api/users")
public class UserRESTController {


    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private AlbumService albumService;


    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {

            model.addAttribute("logged", true);
            model.addAttribute("currentUser", principal.getName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));

        } else {
            model.addAttribute("logged", false);
        }
    }



    // Create new user
    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody LoginRequest credentials) {

        try {
            userService.saveUser(credentials.getUsername(), credentials.getPassword());
            return ResponseEntity.ok(userService.findByName(credentials.getUsername()).get());
        } catch (Exception e) {
            return ResponseEntity.status(405).build();
        }

    }



    // OPERATIONS RESTRICTED TO ADMIN ROLE
    private boolean isAdmin(Model model){

        if ( (boolean)model.getAttribute("logged") && (boolean) (model.getAttribute("admin")) ){
            return true;
        }else{
            return false;
        }
    }



    // Get all users by admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<Collection<User>> getAllUsers(Model model) {

        if (isAdmin(model)){
            return ResponseEntity.ok(userService.findAll());
        }else{
            return ResponseEntity.notFound().build();
        }
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserbyId(@PathVariable long id, Model model) {

        if (isAdmin(model)){

            Optional<User> optionalUser = userService.findById(id);
            if (optionalUser.isPresent()){ // if user exists
                return ResponseEntity.ok(optionalUser.get());
            } else{
                return ResponseEntity.badRequest().build();
            }

        } else {
            // In any other case, return "Not found"
            return ResponseEntity.status(405).build();
        }
    }



    // Operations logged required

    @PreAuthorize("hasRole('USER')")
    // Get current user
    @GetMapping("/me")
    public ResponseEntity<User> me(Model model) {

        // Return only if the user is logged
        if ((boolean) model.getAttribute("logged")){
            // Check if current user is in the database
            Optional<User> optionalUser = userService.findByName((String)model.getAttribute("currentUser"));
            if (optionalUser.isPresent()){
                // return current user
                return ResponseEntity.ok(optionalUser.get());
            }
        }

        // Any other case, return "Not found"
        return ResponseEntity.notFound().build();

    }


    // Update User (Only user can update itself)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody LoginRequest credentials, Model model) {

        String newUsername = credentials.getUsername();
        String newPassword = credentials.getPassword();

        // Check if user is logged
        if ((boolean) model.getAttribute("logged")){

            String currentUsername = (String)model.getAttribute("currentUser");
            try {
                Long userId = userService.findByName(currentUsername).get().getId();
                userService.updateUser(userId, newUsername, newPassword, currentUsername);

                return ResponseEntity.ok(userService.findById(userId).get());

            } catch (Exception e){
                return ResponseEntity.badRequest().build();
            }

        } else {return ResponseEntity.status(405).build();}


    }


    // Delete User (Only admin can delete users and user can delete itself)
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id, Model model) {

        try {
            userService.deleteUser(id, isAdmin(model), (String)model.getAttribute("currentUser"));
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(405).build();
        }

    }

    @PreAuthorize("hasRole('USER')")
    // Get user albums favorites
    @GetMapping("/{id}/albums")
    public ResponseEntity<List<Album>> getUserAlbums(@PathVariable long id, Model model) {

        // User Admin can get all user favorites
        if (isAdmin(model)){
            if (userService.findById(id).isPresent()){
                return ResponseEntity.ok(userService.getUserAlbums(id));
            }
        }
        // User logged is only allowed to get its albums
        if ( (boolean)(model.getAttribute("logged")) ){

            Optional<User> optionalUser = userService.findByName((String)(model.getAttribute("currentUser")));
            // if the user exists in database, and it's id  is equal to the URL one
            if (optionalUser.isPresent() && optionalUser.get().getId() == id) {
                return ResponseEntity.ok(userService.getUserAlbums(id));
            }else{
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.status(405).build();
    }


    @PreAuthorize("hasRole('USER')")
    // Add user album favorite
    @PostMapping("/{id}/albums")
    public ResponseEntity<Album> addUserAlbum(@PathVariable long id, @RequestParam long albumId, Model model) {

        // If the user or the album does not exist, return not found
        Optional<User> user = userService.findById(id);
        Optional<Album> album = albumService.findById(albumId);
        if (!user.isPresent() || (!album.isPresent())) {
            return ResponseEntity.notFound().build();

        } else{
        // if user and album exists
            if (isAdmin(model)){ // user logged is admin, add relation directly
                try {
                    userService.addUserToAlbumFavorites(user.get(), album.get());
                    return ResponseEntity.ok(album.get());
                }catch (Exception e) {
                    return ResponseEntity.status(405).build();
                }

            } else if ( (boolean) (model.getAttribute("logged")) ){
                // if the user exists in database, and it's id  is equal to the URL one
                if (user.isPresent() && user.get().getId() == id) {
                    try {
                        userService.addUserToAlbumFavorites(user.get(), album.get());
                        return ResponseEntity.ok(album.get());
                    }catch (Exception e){
                        return ResponseEntity.status(405).build();
                    }
                }

            }
        }

        return ResponseEntity.status(405).build();

    }

    @PreAuthorize("hasRole('USER')")
    // Remove user album favorite
    @DeleteMapping("/{id}/albums")
    public ResponseEntity<Album> removeUserAlbum(@PathVariable long id, @RequestParam long albumId, Model model) {


        // If the user or the album does not exist, return not found
        Optional<User> user = userService.findById(id);
        Optional<Album> album = albumService.findById(albumId);
        if (!user.isPresent() || (!album.isPresent())) {
            return ResponseEntity.notFound().build();

        } else{
            // if user and album exists
            if (isAdmin(model)){ // user logged is admin, add relation directly
                try {
                    userService.removeUserFromAlbumFavorites(user.get(), album.get());
                    return ResponseEntity.ok().build();
                } catch (Exception e) {
                    return ResponseEntity.status(405).build();
                }

            } else if ( (boolean) (model.getAttribute("logged")) ){
                // if the user exists in database, and it's id  is equal to the URL one
                if (user.isPresent() && user.get().getId() == id) {
                    try {
                        userService.removeUserFromAlbumFavorites(user.get(), album.get());
                        return ResponseEntity.ok().build();
                    } catch (Exception e) {
                        return ResponseEntity.status(405).build();
                    }
                }

            }
        }

        return ResponseEntity.status(405).build();
    }






}