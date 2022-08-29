package com.study.crudtest.controller;

import com.study.crudtest.dto.DetailDTO;
import com.study.crudtest.dto.ListDTO;
import com.study.crudtest.service.interfaces.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final BoardService boardService;

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

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, @CookieValue("id") String memberId){
        DetailDTO post = boardService.getDetail(id, memberId);
        model.addAttribute("post", post);

        return "detail";
    }

}
