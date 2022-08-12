package com.study.crudtest.controller;

import com.study.crudtest.dto.SignUpFormDTO;
import com.study.crudtest.service.interfaces.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final MemberService memberService;

    @GetMapping("/name")
    public String name() {
        return "nyximos";
    }

    @PostMapping("/signup")
    public ResponseEntity userSignup(@RequestBody SignUpFormDTO formDTO) {
        return memberService.signup(formDTO);
    }
}
