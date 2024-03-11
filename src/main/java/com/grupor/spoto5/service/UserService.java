package com.grupor.spoto5.service;

import com.grupor.spoto5.model.User;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
@Service
public class UserService {

    private AtomicLong nextId = new AtomicLong();
    private ConcurrentMap<Long, User> users = new ConcurrentHashMap<>();


    public UserService() {
        // Initialize with some users
       save(new User("user1"));
       save(new User("user2"));
       save(new User("user3"));
       save(new User("user4"));
    }


    public void save(User user) {
        if(user.getId() == null) {
            user.setId(nextId.getAndIncrement());
        }
        this.users.put(user.getId(), user);
    }

    public User findById(Long id) {
        return users.get(id);
    }

    public User findByUserName(String userName) {
        for(User user : users.values()) {
            if(user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public void delete(Long id) {
        users.remove(id);
    }

    public void update(User user) {
        users.put(user.getId(), user);
    }


}
