package com.grupor.spoto5.service;

import com.grupor.spoto5.model.Comment;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CommentService {

    private ConcurrentMap<Long, Comment> comments = new ConcurrentHashMap<>();

    private AtomicLong nextId = new AtomicLong();

    public CommentService () {

    }

    public Collection<Comment> findAll () {
        return comments.values();
    }

    public Comment findById (long id) {
        return comments.get(id);
    }

    public void deleteById (long id) {
        this.comments.remove(id);
    }

    public void saveComment(Comment comment) {

        long id = nextId.getAndIncrement();

        comment.setId(id);
    }
}
