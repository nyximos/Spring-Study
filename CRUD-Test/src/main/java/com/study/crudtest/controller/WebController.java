package com.study.crudtest.controller;

import com.study.crudtest.dto.ListDTO;
import com.study.crudtest.service.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final BoardServiceImpl boardService;

    @GetMapping("/")
    public String index(Model model) {
        List<ListDTO> posts = boardService.getAll();
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "nyximos");
        model.addAttribute("img", "image/cat.jpg");
        return "hello";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/signin")
    public String signin(){
        return "signin";
    }

    @GetMapping("/new")
    public String newPost(){
        return "new";
    }

}
