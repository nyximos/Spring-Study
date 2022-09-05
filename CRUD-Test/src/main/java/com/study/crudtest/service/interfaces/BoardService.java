package com.study.crudtest.service.interfaces;

import com.study.crudtest.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BoardService {

    List<ListDTO> getAll();

    ResponseEntity save(PostFormDTO formDTO);

    ResponseEntity remove(Long id);

    DetailDTO getDetail(Long id, String memberId);

    UpdateDTO getUpdateDTO(Long id);

    ResponseEntity update(Long id, UpdateFormDTO updateFormDTO);
}
