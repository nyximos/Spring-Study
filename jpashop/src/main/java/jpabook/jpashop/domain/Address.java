package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

/*
    값 타입
    변경 불가능하게 설계해야 한다.
    생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만들자.

    JPA 스펙상 엔티티나 임베디드 타입 @Embeddable 은
    자바 기본 생성자를 public 또는 protected로 설정해야한다.
    public 보다는 protected로 설정하는 것이 더 안전하다.

    JPA 구현 라이브러리가 객체를 생성할 때
    리플랙션 같은 기술을 사용할 수 있도록 지원해야 하기 때문에 이런 제약을 둔다.
 */

@Embeddable
@Getter
public class Address {

    private String city;

    private String street;

    private String zipcode;

    protected Address(){

    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
