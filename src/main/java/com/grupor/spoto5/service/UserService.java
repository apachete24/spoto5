package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.User;
import com.grupor.spoto5.repository.AlbumRepository;
import com.grupor.spoto5.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;



@Service
public class UserService {



    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }


    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser (String username, String password) {

        // Avoid empty or null values
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new DuplicateKeyException("Username and password must not be empty");
        }

        Optional<User> user= userRepository.findByName(username);

        // Create if the user is not already created
        if (user.isEmpty()) {
            userRepository.save(new User(username, passwordEncoder.encode(password), "USER"));
        } else {
            throw new DuplicateKeyException("El nombre de usuario '" + username + "' ya está en uso.");
        }




    }


    // Save permanently a user in the database
    public void save(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario no puede estar vacío");
        }

        if (user.getEncodedPassword() == null || user.getEncodedPassword().isEmpty()) {
            throw new IllegalArgumentException("La contraseña del usuario no puede estar vacía");
        }

        userRepository.save(user);
    }





    public Optional<User> findByName(String username) {
        return userRepository.findByName(username);
    }


    public void updateUser (Long id, String username, String password, String currentUser) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("El nombre del usuario no puede estar vacío");
            }

            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("La contraseña del usuario no puede estar vacía");
            }

            User userAux = user.get();

            if (currentUser.equals(userAux.getName())) {
                userAux.setName(username);
                userAux.setEncodedPassword(passwordEncoder.encode(password));
                userRepository.save(userAux);
            } else {
                throw new AccessDeniedException("Permision denied");
            }

        } else {
            throw new EntityNotFoundException("User not found");
        }

    }


    public void deleteUser(long id, boolean isAdmin, String currentUser) throws AccessDeniedException {
        User user = userRepository.findById(id).orElseThrow();
        if (isAdmin) {
            userRepository.deleteById(id);
        } else if (currentUser.equals(user.getName())) {
            userRepository.deleteById(id);
        } else {
            throw new AccessDeniedException("DENIED ACTION");
        }
    }



    public List<Album> getUserAlbums(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get().getAlbumFavs();
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }


    @Transactional
    public void addUserToAlbumFavorites(User user, Album album) {
        // Verify if the album and user exist
        Optional<User> existingUser = userRepository.findById(user.getId());
        Optional<Album> existingAlbum = albumRepository.findById(album.getId());

        if (existingUser.isPresent() && existingAlbum.isPresent()) {

            // Verify if the relation is not already created
            if (existingUser.get().getAlbumFavs().contains(album)) {
                throw new  DuplicateKeyException("The user is already in the album's favorites");
            }
            // Add the user to the album's favorites
            album.getUserFavs().add(user);
            // Add the album to the user's favorites
            user.getAlbumFavs().add(album);

            // Saved changes in the database
            userRepository.save(user);
            albumRepository.save(album);

        } else {
            throw new EntityNotFoundException("User or album not found");
        }
    }



    public void removeUserFromAlbumFavorites(User user, Album album) {
        album.getUserFavs().remove(user);
        user.getAlbumFavs().remove(album);
        userRepository.save(user);
    }



}
