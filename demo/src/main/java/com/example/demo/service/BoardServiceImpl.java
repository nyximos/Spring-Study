package com.example.demo.service;

import com.example.demo.domain.Board;
import com.example.demo.domain.Member;
import com.example.demo.dto.BoardListDTO;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.interfaces.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
                    .content(board.getContent())
                    .createdAt(board.getCreatedAt())
                    .views(board.getViews())
                    .memberId(member.getId())
                    .build();

            boardListDTOs.add(dto);
        }

        return boardListDTOs;
    }
}
