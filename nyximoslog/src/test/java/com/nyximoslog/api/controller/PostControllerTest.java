package com.nyximoslog.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/post 요청시 Hello World를 출력한다.")
    void test() throws Exception {

        // expected
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
//                .andExpect(content().string("Hello World"))
                .andExpect(content().string("Hello World"))
                .andDo(print());
    }
    
    @Test
    void postTest() throws Exception {

        // 글 제목
        // 글 내용
        // 사용자
            // id
            // name
            // level

        // expect
        mockMvc.perform(post("/posts")   // application/xxx-form-encoded 형태로 보냄 (content-type : http header 값)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "글 제목입니다.")
                        .param("content", "글 내용입니다. 하하")
        )
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(print());
    }

    @Test
    void postJsonTest() throws Exception {

        // 글 제목
        // 글 내용
        // 사용자
            // id
            // name
            // level

        // expect
        mockMvc.perform(post("/posts")   // appplication/json 형태로 보냄 (content-type : http header 값)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다\"}") // ModelAttribute로 받으면 값이 안들어가짐
        )
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(print());
    }

    @Test
    @DisplayName("/post 요청시 title 값은 필수다.")
    void postTest2() throws Exception {

        // expect
        mockMvc.perform(post("/posts")   // appplication/json 형태로 보냄 (content-type : http header 값)
                .contentType(MediaType.APPLICATION_JSON)
                // {"title":""}
                // {"title":null}
                .content("{\"title\": null, \"content\": \"내용입니다\"}") // ModelAttribute로 받으면 값이 안들어가짐
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }
    

}