package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)  // spring이랑 같이 엮어서 실행
@SpringBootTest               // springBoot 띄운 상태에서 테스트
@Transactional                // 변경 작업할 때 필수, 롤백 가능
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
//    @Autowired
//    EntityManager em;

    @Test
    // @Rollback(false) // spring에서 @Transactional이 테스트케이스에 있으면 기본적으로 커밋을 안하고 롤백을 한다.
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);  // 영속성 컨텍스트에 들어감

        //then
//        em.flush();  // 영속성 컨텍스트의 변경 내용을 db에 반영
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //Given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //When
        memberService.join(member1);
        memberService.join(member2); //예외가 발생해야 한다.

        //Then
        fail("예외가 발생해야 한다.");

    }

}