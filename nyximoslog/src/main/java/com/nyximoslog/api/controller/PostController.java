package com.nyximoslog.api.controller;

import com.nyximoslog.api.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
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

    /*
    @PostMapping("/posts")
    public String post(@RequestBody PostCreate params) throws Exception {

        // 데이터를 검증하는 이유

        // 1. client 개발자가 깜박할 수 있다. 실수로 값을 안보낼 수 있다.
        // 2. client bug로 값을 누락될 수 있다.
        // 3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼 수 있다.
        // 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
        // 5. 서버 개발자의 편안함을 위해서

        log.info("params={}", params.toString());

        String title = params.getTitle();
        if(title == null || title.equals("")){
            // 1. 빡세다. (노가다)
            // 2. 개발팁 -> 무언가 3번 이상 반복 작업을 할 때 내가 뭔가 잘못하고 있는 건 아닐지 의심한다.
            // 3. 누락 가능성
            // 4. 생각보다 검증해야할 게 많다. (꼼꼼하지 않을 수 있다.) - 빈 string, 수십억 글자 등
            // 5. 뭔가 개발자 스럽지 않다. -> 간지 X
            throw new Exception("타이틀 값이 없어요!");
        }

        String content = params.getContent();
        if (content == null || content.equals("")){
            // error
        }

        return "Hello World";
    }
     */

    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate params, BindingResult result) {

        if(result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);
            String fieldName = firstFieldError.getField();// title
            String errorMessage = firstFieldError.getDefaultMessage();// 에러 메시지

            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMessage);
            return error;
        }


        return Map.of();
    }

}
