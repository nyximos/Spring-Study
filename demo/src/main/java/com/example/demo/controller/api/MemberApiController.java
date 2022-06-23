package com.example.demo.controller.api;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.MyResponse;
import com.example.demo.service.MemberServiceImpl;
import com.example.demo.service.interfaces.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {

    private final MemberServiceImpl memberService;

    @PostMapping("/login")
    public ResponseEntity<MyResponse> login(@RequestBody LoginDTO loginDTO) {
        ResponseEntity<MyResponse> responseEntity = memberService.login(loginDTO);
        return responseEntity;
    }
}
