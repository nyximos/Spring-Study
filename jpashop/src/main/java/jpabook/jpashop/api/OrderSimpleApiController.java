package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/*
    xToOne 관계 성능 최적화 (ManyToOne, OneToOne)
    Order
    Order -> Member
    Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository; // 의존관계

    /*
    * V1. 엔티티 직접 노출
    * - Hibernate5Module 모듈 등록, LAZY=null 처리
    * - 양방향 관계 문제 발생 -> 한 곳을 @JsonIgnore 처리 -> 안하면 서로 호출하면서 무한 루프 걸림
    *
    * 엔티티를 API 응답으로 외부로 노출하는 것은 좋지 않다.
    * Hibernate5Module을 사용하기 보다는 DTO로 변환해서 반환하자.
    *
    * 지연 로딩 LAZY을 피하기 위해 즉시로딩 EAGER으로 설정하면 안된다!
    * 즉시 로딩 때문에 연관관계가 필요 없는 경우에도 데이터를 항상 조회해서 성능 문제가 발생할 수 있다.
    * 즉시 로딩으로 설정 하면 성능 튜닝이 매우 어려워진다.
    * 항상 지연 로딩을 기본으로 하고 성능 최적화가 필요한 경우 fetch join을 사용하자.
    */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {

        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기환
        }

        return all;
    }

    /*
        V2. 엔티티를 조회해서 DTO로 변환 (fetch join 사용x)
            - 엔티티를 DTO로 변환하는 일반적인 방법
            - 단점 : 지연로딩으로 쿼리 N번 호출
            - 지연 로딩은 영속성 컨텍스트에서 조회하므로, 이미 조회된 경우 쿼리를 생략한다.
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {

        // Order 2개
        // N + 1 -> 1 + N(2)  ( 회원 N + 배송 N)
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        // 2개
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;

        /*
        return orderRepository.findAllByString(new OrderSearch()).stream()
                .map(SimpleOrderDto::new)
                .collect(toList());

         */
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate; // 주문시간
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // Lazy 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // Lazy 초기화
        }
    }

    /*
        V3. 엔티티를 조회해서 DTO로 변환 (fetch join 사용 O)
            - fetch join으로 쿼리 1번 호출
            - 참고 fetch join에 대한 자세한 내용은 JPA 기본편 참조 (정말 중요함)
            - 대부분의 성능 이슈가 해결된다.
            - 장점 : 재사용성이 높음
            - 단점 : select절에서 다 긁어옴
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }

    /*
        V4. JPA에서 DTO로 바로 조회
            - 쿼리 1번 호출
            - select 절에서 원하는 데이터만 선택해서 조회
            - 일반적인 SQL을 사용할 때처럼 원하는 값을 선택해서 조회
            - new 명령어를 사용해서 JPQL의 결과를 DTO로 즉시 변환
            - SELECT 절에서 원하는 데이터를 직접 선택하므로 DB -> 애플리케이션 네트웍 용량 최적화(생각보다 미비)
            - 단점 : Repository재사용성 떨어짐, API 스펙에 맞춘 코드가 Repository에 들어간다.
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
        return orderSimpleQueryRepository.findOrderDtos();
    }
}