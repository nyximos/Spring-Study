package com.study.crudtest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "nyximos");
        model.addAttribute("img", "image/cat.jpg");
        return "hello";
    }


}
