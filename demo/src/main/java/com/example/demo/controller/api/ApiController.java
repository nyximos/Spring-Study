package com.example.demo.controller.api;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.MyResponse;
import com.example.demo.dto.PostDTO;
import com.example.demo.service.BoardServiceImpl;
import com.example.demo.service.MemberServiceImpl;
import com.example.demo.service.interfaces.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private final MemberServiceImpl memberService;
    private final BoardServiceImpl boardService;

    @PostMapping("/login")
    public ResponseEntity<MyResponse> login(@RequestBody LoginDTO loginDTO) {
        ResponseEntity<MyResponse> responseEntity = memberService.login(loginDTO);
        return responseEntity;
    }

    @PostMapping("/board")
    public ResponseEntity<MyResponse> save(@RequestBody PostDTO postDTO) {
        ResponseEntity<MyResponse> responseEntity = boardService.save(postDTO);
        return responseEntity;
    }
    @DeleteMapping("/board/{id}")
    public ResponseEntity<MyResponse> remove(@PathVariable Long id) {
        ResponseEntity<MyResponse> responseEntity = boardService.remove(id);
        return responseEntity;
    }

    @PatchMapping("/board/{id}")
    public ResponseEntity<MyResponse> view(@PathVariable Long id) {
        ResponseEntity<MyResponse> responseEntity = boardService.view(id);
        return responseEntity;
    }
}
