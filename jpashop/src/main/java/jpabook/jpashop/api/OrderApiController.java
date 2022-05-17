package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    /*
     * V1. 엔티티 직접 노출
     * - 엔티티가 변하면 API 스펙이 변한다.
     * - 트랜잭션 안에서 지연 로딩 필요
     * - 양방향 연관관계 문제, 한 곳에 @JsonIgnore를 추가해야 한다.
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            // proxy 강제 초기화화
           List<OrderItem> orderItems = order.getOrderItems();
            /*for (OrderItem orderItem : orderItems) {
                orderItem.getItem().getName();
            }*/
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    /*
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용 X)
     * - 트랜잭션 안에서 지연 로딩 필요
     *
     * 지연 로딩은 영속성 컨텍스트에 있으면
     * 영속성 컨텍스트에 있는 엔티티를 사용하고 없으면 SQL을 실행한다.
     * 같은 영속성 컨텍스트에서 이미 로딩한 회원 엔티티를 추가로 조회하면 SQL을 실행하지 않는다.
     */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> orderV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;  // 주문시간
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;


        public OrderDto(Order order) {
            orderId =  order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(toList());
        }
    }

    @Data
    static class OrderItemDto {
        private String itemName;  // 상품명
        private int orderPrice;   // 주문 가격
        private int count;        // 주문 수량


        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }


    /*
     * V3. 엔티티를 조회해서 DTO로 변환 (fetch join 사용 O)
     * - distinct를 사용한 이유는 1대다 조인이 있으므로 데이터베이스 row가 증가한다.
     *   그 결과 같은 order 엔티티의 조회 수도 증가하게 된다.
     * - JPA의 distinct는 SQL에 distinct를 추가하고,
     *   더해서 같은 엔티티가 조회되면, 애플리케이션에서 중복을 걸러준다.
     * - 이 예에서 order가 컬렉션 페치 조인 때문에 중복 조회되는 것을 막아준다.
     *
     * 단점
     * - 페이징 시에는 N 부분을 포기해야함(대신에 batch fetch size? 옵션 주면 N -> 1 쿼리로 변경가능)
     *
     * - 컬렉션 fetch join을 사용하면 페이징이 불가능하다.
     *   하이버네이트는 경고 로그를 남기면서 모든 데이터를 DB에서 읽어오고, 메모리에서 페이징 해버린다.(매우 위험)
     *
     * - 컬렉션 fetch join은 1개만 사용할 수 있다.
     *   컬렉션 둘 이상에 fetch join을 사용하면 안된다.
     *   데이터가 부정합하게 조회될 수 있다.
     *
     * - 중복 데이터를 DB에서 애플리케이션으로 다 전송 -> 전송량이 많아짐
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());

        return result;
    }

    /*
     * V3.1 엔티티를 조회해서 DTO로 변환 - 페이징과 한계 돌파
     * - ToOne 관계를 모두 fetch join 한다.
     *   ToOne 관계는 row수를 증가시키지 않으므로 페이징 쿼리에 영향을 주지 않는다.
     * - 컬렉션은 지연 로딩으로 조회한다.
     * - 지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size, @BatchSize 적용
     *   컬렉션이나 프록시 객체를 한꺼번에 설정한 size만큼 IN 쿼리로 조회한다.
     *
     * 장점
     * - 쿼리 호출 수가 1+N -> 1+1 로 최적화한다.
     * - 조인보다 DB 데이터 전송량이 최적화 된다. (Order가 OrderItem만큼 중복해서 조회된다. 각각 조회하므로 전송해야 할 중복 데이터가 없다.)
     * - fetch join 방식과 비교해서 쿼리 호출 수가 약간 증가 하지만, DB 데이터 전송량이 감소한다.
     * - 컬렉션 fetch join은 페이징이 불가능 하지만 이 방법은 페이징이 가능하다.
     *
     * 결론
     * - ToOne 관계는 fetch join해도 페이징에 영향을 주지 않는다.
     *   ToOne 관계는 fetch join으로 쿼리수를 줄이고 해결하고
     *   나머지는 hibernate.default_batch_fetch_size로 최적화하자.
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit){
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());

        return result;
    }
}
