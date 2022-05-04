package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
    엔티티에는 Setter를 사용하지 말자.
    변경 포인트가 많아서 유지보수가 어렵다.
 */
@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // 연관관계의 거울, Order 테이블의 member필드에 의해 맵핑된거야 (읽기전용v)
    private List<Order> orders = new ArrayList<>();
}
