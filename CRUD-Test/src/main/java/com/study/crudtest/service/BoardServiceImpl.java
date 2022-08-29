package com.study.crudtest.service;

import com.study.crudtest.domain.Board;
import com.study.crudtest.domain.Member;
import com.study.crudtest.domain.MemberRole;
import com.study.crudtest.dto.DetailDTO;
import com.study.crudtest.dto.ListDTO;
import com.study.crudtest.dto.PostFormDTO;
import com.study.crudtest.exception.MemberNotExistException;
import com.study.crudtest.repository.BoardRepository;
import com.study.crudtest.repository.MemberRepository;
import com.study.crudtest.service.interfaces.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Override
    public List<ListDTO> getAll() {

        List<Board> posts = boardRepository.findAll();
        List<ListDTO> list = new ArrayList<>();


        for (Board post : posts) {
            Member member = post.getMember();

            ListDTO dto = ListDTO.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .createdAt(post.getCreatedAt())
                    .userViews(post.getUserViews())
                    .adminViews(post.getAdminViews())
                    .memberName(member.getName())
                    .build();

            list.add(dto);
        }

        return list;
    }

    @Override
    public ResponseEntity save(PostFormDTO formDTO) {

        /*
        Member findMember = memberRepository.findById(formDTO.getMemberId()) // 회원을 찾아서 Optional로 감싸서 리턴
                .orElseThrow(() -> new MemberNotExistException());  // Optional에서 값을 꺼냈는데 만약 없다면 정의한 예외 던짐
        Board post = Board.builder()
                .title(formDTO.getTitle())
                .content(formDTO.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .adminViews(0)
                .userViews(0)
                .likes(0)
                .member(findMember)
                .build();        boardRepository.save(post);
        return new ResponseEntity("success",HttpStatus.OK);
         */

        Optional<Member> member = memberRepository.findById(formDTO.getMemberId());
        if(member.isPresent()){
            Member memberEntity = member.get();

            Board post = Board.builder()
                    .title(formDTO.getTitle())
                    .content(formDTO.getContent())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .adminViews(0)
                    .userViews(0)
                    .likes(0)
                    .member(memberEntity)
                    .build();

            boardRepository.save(post);

            return new ResponseEntity("success", HttpStatus.OK);
        } else {
            return new ResponseEntity("fail", HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity remove(Long id) {

        boardRepository.deleteById(id);
        return new ResponseEntity("success", HttpStatus.OK);

    }

    @Override
    public DetailDTO getDetail(Long id, String memberId) {

        Optional<Board> board = boardRepository.findById(id);
        Board boardEntity = board.orElse(null);

        Member member = boardEntity.getMember();

        if(!memberId.equals(member.getId())){
            if(member.getRole().equals(MemberRole.ADMIN)){
                boardEntity.countAdmin();
            }else {
                boardEntity.countUser();
            }
        }

        DetailDTO detailDTO = DetailDTO.builder()
                .id(boardEntity.getId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .createdAt(boardEntity.getCreatedAt())
                .updatedAt(boardEntity.getUpdatedAt())
                .userViews(boardEntity.getUserViews())
                .adminViews(boardEntity.getAdminViews())
                .memberName(member.getName())
                .build();

        return detailDTO;
    }


}
