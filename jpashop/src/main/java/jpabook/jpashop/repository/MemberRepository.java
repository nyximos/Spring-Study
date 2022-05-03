package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    /**
    @Autowired @PersistenceContext      // 스프링이 엔티티 매니저 생성해서 주입해준다.
    private EntityManager em;

    public MemberRepository(EntityManager em) {
        this.em = em;
    }*/

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);  // 영속성 컨텍스트에 엔티티를 넣음, 트랜잭션이 커밋되는 시점에 DB에 Insert 쿼리문이 날아감
   }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)  // from의 대상이 테이블이 아닌 엔티티
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
