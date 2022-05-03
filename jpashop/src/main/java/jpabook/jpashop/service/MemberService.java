package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)  // 읽기 성능 최적화
@RequiredArgsConstructor         // final 있는 필드만
public class MemberService {

    /**
    @Autowired   // field injection 주입 까다로움
    private MemberRepository memberRepository;

    @Autowired   // setter injection 동작중 바뀔 가능성 있음
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    */

    //  constructor injection  중간에 바꿀 수 없어서 좋다.
    private final MemberRepository memberRepository;  //  값 세팅안했을 때 컴파일에러

    /*
     * 회원가입
     */
    @Transactional  // 데이터를 변경할 땐 트랜잭션 안에서
    public Long join(Member member) {
        validateDuplicateMember(member);  // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
