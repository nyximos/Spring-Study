package com.example.demo.service.interfaces;

import com.example.demo.dto.BoardDetailDTO;
import com.example.demo.dto.BoardListDTO;

import java.util.List;

public interface BoardService{
    List<BoardListDTO> getAllDTO();

    BoardDetailDTO getDetail(Long id);

}
