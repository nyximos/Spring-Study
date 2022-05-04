package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    /*
        @ManyToMany
        중간 테이블에 컬럼을 추가할 수 없고 세밀하게 쿼리를 실행하기 어렵기때문에
        실무에서 사용하는데 한계가 있다.
        중간 테이블을 만들고 @ManyToOne, @OneToMany로 매핑해서 사용하자.
     */
    @ManyToMany
    @JoinTable(name = "category_item",
    joinColumns = @JoinColumn(name = "category_id"),
    inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //==연관관계 메소드==//
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }

}
