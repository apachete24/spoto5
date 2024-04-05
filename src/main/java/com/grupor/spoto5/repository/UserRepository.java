package com.grupor.spoto5.repository;

import com.grupor.spoto5.model.Comment;
import com.grupor.spoto5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
