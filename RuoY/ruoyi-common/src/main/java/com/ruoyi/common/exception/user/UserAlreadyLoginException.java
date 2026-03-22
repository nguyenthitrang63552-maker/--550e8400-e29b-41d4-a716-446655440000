package com.ruoyi.common.exception.user;

public class UserAlreadyLoginException extends UserException {
    public UserAlreadyLoginException() {
        super("user.already.login", null);
    }
}
