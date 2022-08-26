package com.study.crudtest.controller;

import com.study.crudtest.dto.LoginDTO;
import com.study.crudtest.dto.PostFormDTO;
import com.study.crudtest.dto.SignUpFormDTO;
import com.study.crudtest.service.interfaces.BoardService;
import com.study.crudtest.service.interfaces.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final MemberService memberService;
    private final BoardService boardService;

    @GetMapping("/name")
    public String name() {
        return "nyximos";
    }

    @PostMapping("/signup")
    public ResponseEntity signin(@RequestBody SignUpFormDTO formDTO) {
        return memberService.signup(formDTO);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO){
        return memberService.login(loginDTO);
    }

    @PostMapping("/posts")
    public ResponseEntity save(@RequestBody PostFormDTO formDTO){
        ResponseEntity responseEntity = boardService.save(formDTO);
        return responseEntity;
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity remove(@PathVariable Long id) {
        ResponseEntity responseEntity = boardService.remove(id);
        return responseEntity;
    }
}
