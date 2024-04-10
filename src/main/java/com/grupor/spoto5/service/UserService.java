package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Album;
import com.grupor.spoto5.model.User;
import com.grupor.spoto5.repository.AlbumRepository;
import com.grupor.spoto5.repository.UserRepository;
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


    public void addUserToAlbumFavorites(User user, Album album) {
        album.getUserFavs().add(user);
        user.getAlbumFavs().add(album);
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
