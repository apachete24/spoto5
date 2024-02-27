package com.grupor.spoto5.controller;

import com.grupor.spoto5.service.AlbumService;
import com.grupor.spoto5.service.CommentService;
import com.grupor.spoto5.service.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserSession userSession;

    @GetMapping("/album/{id}")
    public String showComments(Model model, @PathVariable long id) {


    }




}
