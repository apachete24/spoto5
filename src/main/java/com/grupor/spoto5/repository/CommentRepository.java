package com.grupor.spoto5.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupor.spoto5.model.Comment;

public interface CommentRepository extends JpaRepository <Comment, Long> {

}
