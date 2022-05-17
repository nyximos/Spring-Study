package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /*
        컬렉션은 필드에서 초기화하는 것이 안전하다.
        null 문제에서 안전하다.

        하이버네이트는 엔티티를 영속화 할 때,
        컬랙션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경한다.

        만약 getOrders()처럼 임의의 메서드에서 컬렉션을 잘못 생성하면
        하이버네이트 내부 메커니즘에 문제가 발생할 수 있다.
        따라서 필드 레벨에서 생성하는 것이 가장 안전하고 코드도 간결하다.

        Member member = new Member();
        System.out.println(member.getOrders().getClass());
        em.persist(team);
        System.out.println(member.getOrders().getClass());

        //출력 결과
        class java.util.ArrayList
        class org.hibernate.collection.internal.PersistentBag
     */
    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    /*
        모든 연관관계는 지연로딩으로 설정하자.
        즉시로딩 EAGER는 예측이 어렵고 어떤 SQL이 실행될지 어렵다.
        특히 JOQL을 실행할 때 N+1 문제가 자주 발생한다.

        실무에서 모든 연관관계는 지연로딩 LAZY로 설정해야 한다.

        연관된 엔티티를 함께 DB에서 조회해야 한다면, fetch join 또는 엔티티 그래프 기능을 사용한다.
        @xToONE 관계는 default가 즉시로딩이므로 직접 지연로딩으로 설정해주자.
     */
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태

    //==연관관계 메서드==//
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드===//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem:orderItems){
            orderItem.cancel();
        }
    }

    //==조회 로직==//

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;

        // return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }

}
