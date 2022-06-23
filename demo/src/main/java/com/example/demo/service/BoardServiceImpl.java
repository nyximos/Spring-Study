package com.example.demo.service;

import com.example.demo.domain.Board;
import com.example.demo.domain.Member;
import com.example.demo.dto.BoardDetailDTO;
import com.example.demo.dto.BoardListDTO;
import com.example.demo.dto.MyResponse;
import com.example.demo.dto.StatusEnum;
import com.example.demo.repository.BoardRepository;
import com.example.demo.service.interfaces.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public List<BoardListDTO> getAllDTO() {

        List<Board> boards = boardRepository.findAll();
        List<BoardListDTO> boardListDTOs = new ArrayList<>();

        for (Board board : boards) {
            Member member = board.getMember();

            BoardListDTO dto = BoardListDTO.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .createdAt(board.getCreatedAt())
                    .views(board.getViews())
                    .memberId(member.getId())
                    .build();

            boardListDTOs.add(dto);
        }

        return boardListDTOs;
    }

    @Override
    public BoardDetailDTO getDetail(Long id) {

        Optional<Board> board = boardRepository.findById(id);
        Board boardEntity = board.orElse(null);

        Member member = boardEntity.getMember();

        BoardDetailDTO detailDTO = BoardDetailDTO.builder()
                .id(boardEntity.getId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .createdAt(boardEntity.getCreatedAt())
                .views(boardEntity.getViews())
                .memberId(member.getId())
                .build();

        return detailDTO;
    }

    @Override
    public ResponseEntity<MyResponse> remove(Long id) {

        boardRepository.deleteById(id);

        MyResponse body = MyResponse.builder()
                .header(StatusEnum.OK)
                .message("성공")
                .build();

        return new ResponseEntity<MyResponse>(body, HttpStatus.OK);
    }

}
