package com.monkeypenthouse.core.common;

public class SocialLoginRes<T> extends DefaultRes<T> {

    private final boolean loginSucceeded;

    public boolean getLoginSucceeded() {
        return loginSucceeded;
    }

    public SocialLoginRes(final int statusCode, final String responseMessage, final T data, boolean loginSucceeded) {
        super(statusCode, responseMessage, data);
        this.loginSucceeded = loginSucceeded;
    }

    public static<T> SocialLoginRes<T> res(final int statusCode, final String responseMessage, boolean loginSucceeded) {
        return res(statusCode, responseMessage, null, loginSucceeded);
    }

    public static<T> SocialLoginRes<T> res(final int statusCode, final String responseMessage, final T t, boolean loginSucceeded) {
        return new SocialLoginRes<>(statusCode, responseMessage, t, loginSucceeded);
    }
}
