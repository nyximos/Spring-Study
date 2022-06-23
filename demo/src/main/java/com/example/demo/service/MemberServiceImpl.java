package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.MyResponse;
import com.example.demo.dto.StatusEnum;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.interfaces.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity<MyResponse> login(LoginDTO loginDTO) {

        Optional<Member> member = memberRepository.findById(loginDTO.getId());
        Member memberEntity = member.orElse(null);

        if (member==null) {
            MyResponse body = MyResponse.builder()
                    .header(StatusEnum.BAD_REQUEST)
                    .message("해당 아이디를 가진 회원이 없습니다.")
                    .build();

            return new ResponseEntity<MyResponse>(body, HttpStatus.OK);
        }

        if (memberEntity.getPassword().equals(loginDTO.getPassword())) {
            MyResponse body = MyResponse.builder()
                    .header(StatusEnum.OK)
                    .message("성공")
                    .build();

            return new ResponseEntity<MyResponse>(body, HttpStatus.OK);
        } else {
            MyResponse body = MyResponse.builder()
                    .header(StatusEnum.BAD_REQUEST)
                    .message("비밀번호가 틀립니다.")
                    .build();

            return new ResponseEntity<MyResponse>(body, HttpStatus.OK);
        }
    }
}
