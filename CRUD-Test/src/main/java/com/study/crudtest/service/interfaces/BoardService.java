package com.study.crudtest.service.interfaces;

import com.study.crudtest.dto.ListDTO;
import com.study.crudtest.dto.PostFormDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BoardService {

    List<ListDTO> getAll();

    ResponseEntity save(PostFormDTO formDTO);

    ResponseEntity remove(Long id);
}
