package com.example.demo.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name ="member_id", length = 20, nullable = false)
    private String id;

    @Column(name = "member_password", nullable = false)
    private String password;

    @Column(name = "member_name")
    private String name;
}
