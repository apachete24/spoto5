package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.User;
import com.grupor.spoto5.repository.AlbumRepository;
import com.grupor.spoto5.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumRepository albumRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        // Initialize with some users
       save(new User("user1"));
       save(new User("user2"));
       save(new User("user3"));
       save(new User("user4"));
    }


    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public List<User> findByIds(List<Long> ids) { return userRepository.findAllById(ids);}

    public boolean exist(long id) {
        return userRepository.existsById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
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

    /*
    public void update(User updatedUser) {
        User user = userRepository.findById(updatedUser.getId()).orElseThrow();
        user.setUserName(updatedUser.getUserName());
        user.getAlbumFavs().forEach(updatedUser::addAlbumFav);
        userRepository.save(user);
    }
    */

}
