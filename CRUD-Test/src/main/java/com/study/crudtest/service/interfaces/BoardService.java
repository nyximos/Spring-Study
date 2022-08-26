package com.study.crudtest.service.interfaces;

import com.study.crudtest.dto.PostFormDTO;
import org.springframework.http.ResponseEntity;

public interface BoardService {

    ResponseEntity save(PostFormDTO formDTO);

    ResponseEntity remove(Long id);
}
