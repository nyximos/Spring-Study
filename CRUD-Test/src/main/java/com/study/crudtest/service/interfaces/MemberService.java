package com.study.crudtest.service.interfaces;

import com.study.crudtest.dto.SignUpFormDTO;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    ResponseEntity signup(SignUpFormDTO formDTO);
}
