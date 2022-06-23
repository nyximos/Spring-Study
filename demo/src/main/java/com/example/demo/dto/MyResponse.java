package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyResponse<T> {

    private StatusEnum header;
    private String message;
    private T body;

    @Builder
    public MyResponse(StatusEnum header, String message, T body) {
        this.header = header;
        this.message = message;
        this.body = body;
    }
}
