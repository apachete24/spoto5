package com.grupor.spoto5.repository;

import com.grupor.spoto5.model.Comment;
import com.grupor.spoto5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
}
