package com.nyximoslog.api.controller;

import com.nyximoslog.api.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
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

    // Http Method
    // GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE, CONNECT 기능, 특징 공부하기
    // 글 등록
    // POST Method

    /*
    @PostMapping("/posts")
    public String post(@RequestParam String title, @RequestParam String content) {
        log.info("title={}, content{}", title, content);
        return "Hello World";
     */

    /*
    @GetMapping
    public String post(@RequestParam Map<String, String> params) {
        log.info("params={}", params);
        String title = params.get("title");
        return "Hello World";
    }
     */

    /*
    @PostMapping("/posts")
    public String post(@ModelAttribute PostCreate params) {
        log.info("params={}", params.toString());
        return "Hello World";
    }
     */

    @PostMapping("/posts")
    public String post(@RequestBody PostCreate params) {
        log.info("params={}", params.toString());
        return "Hello World";
    }

}
