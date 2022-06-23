package com.example.demo.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue
    @Column(name = "board_id", nullable = false)
    private Long id;

    @Column(name = "board_password", nullable = false)
    private String password;

    @Column(name = "board_title", nullable = false)
    private String title;

    @Column(name = "board_content", length = 1000, nullable = false)
    private String content;

    @Column(name = "board_created_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd`T`HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "views", nullable = false)
    private int views;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void count() {
        this.views++;
    }

}
