package com.sky.exception;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @AUTHOR: djl
 * @DATE: 2023/8/10 20:49
 */
public class AccountNotAuthException extends BaseException {
    public AccountNotAuthException() {
    }

    public AccountNotAuthException(String msg) {
        super(msg);
    }
}
