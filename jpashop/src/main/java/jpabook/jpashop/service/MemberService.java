package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
    @Transactional : 트랜잭션, 영속성 컨텍스트

    readOnly = true
    데이터 변경이 없는 읽기 전용 메서드에 사용
    영속성 컨텍스트를 플러시 하지 않으므로 약간의 성능 향상 (읽기 전용에는 다 적용)
    데이터베이스 드라이버가 지원하면 DB에서 성능 향상
 */

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

    /*
        constructor injection  중간에 바꿀 수 없어서 좋다.

        변경 불가능한 객체 생성 가능
        생성자가 하나면 @Autowired 생략 가능

        final 키워드를 추가하면 컴파일 시점에 memberRepository를 설정하지 않는 오류를 체크할 수 있다.
        보통 기본 생성자를 추가할 때 발견
     */
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

    /*
        회원 수정
        Member를 반환해도 되지만, Command와 Query가 같이 있는 꼴이 된다.

        Command와 Query를 분리하라. CQS
        이 메서드를 호출 했을 때, 내부에서 변경(사이드 이펙트)가 일어나는 메서드인지, 아니면 내부에서 변경이 전혀 일어나지 않는 메서드인지 명확히 분리
     */
    @Transactional
    public void update(Long id, String name) {      // update는 변경성 메서드
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
