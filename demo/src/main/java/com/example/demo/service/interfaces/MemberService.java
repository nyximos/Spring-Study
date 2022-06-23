package com.example.demo.service.interfaces;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.MyResponse;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    ResponseEntity<MyResponse> login(LoginDTO loginDTO);
}
