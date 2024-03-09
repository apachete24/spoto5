package com.grupor.spoto5.restControler;

import com.grupor.spoto5.model.Comment;
import com.grupor.spoto5.service.AlbumService;
import com.grupor.spoto5.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentRESTController {
    @Autowired
    private CommentService comments;

    @GetMapping("/comments")
    public Collection<Comment> getComments() {
        return comments.getComments();
    }


}
