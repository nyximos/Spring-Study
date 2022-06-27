package com.nyximoslog.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    // SSR -> jsp, thymeleaf, mustache, freemarker
            // -> html rendering
    // SPA ->
            // -> javascript + <-> API (JSON)
    //  vue -> vue + SSR => nuxt.js
    //  react -> react + SSR => next.js

//    @RequestMapping(method = RequestMethod.GET, path = "/v1/posts")
    @GetMapping("/posts")
    public String get() {
        return "Hello World";
    }

}
