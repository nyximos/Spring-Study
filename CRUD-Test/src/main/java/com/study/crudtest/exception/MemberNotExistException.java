package com.study.crudtest.exception;

public class MemberNotExistException extends RuntimeException {

    private final int ERR_CODE = 404;

    public MemberNotExistException() {
        super("회원이 존재하지 않습니다.");
    }

    public int getErrCode() {
        return ERR_CODE;
    }
}