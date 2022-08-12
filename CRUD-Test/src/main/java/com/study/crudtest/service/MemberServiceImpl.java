package com.study.crudtest.service;

import com.study.crudtest.domain.Member;
import com.study.crudtest.domain.MemberRole;
import com.study.crudtest.dto.SignUpFormDTO;
import com.study.crudtest.repository.MemberRepository;
import com.study.crudtest.service.interfaces.MemberService;
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

    public ResponseEntity signup(SignUpFormDTO formDTO) {

        Optional<Member> member = memberRepository.findById(formDTO.getId());

        if (member.isEmpty()) {
            Member newMember = Member.builder()
                    .id(formDTO.getId())
                    .password(formDTO.getPassword())
                    .name(formDTO.getName())
                    .role(MemberRole.USER)
                    .build();

            memberRepository.save(newMember);

            return new ResponseEntity("success", HttpStatus.OK);
        } else {
            return new ResponseEntity("fail", HttpStatus.OK);
        }
    }
}
