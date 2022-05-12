package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    /*
        논리적 계층이 깨져있음
        레퍼지토리가 화면에 의존적임 -> Repository는 엔티티를 조회하는데 써야한다.
        성능최적화된 커리는 별도의 패키지를 만들어 뽑아낸다.
        통계형 api등 복잡한 조인 쿼리를 사용하여 DTO를 뽑아내야 할 때,
        Query Repository를 뽑아낸다.
     */
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
}
