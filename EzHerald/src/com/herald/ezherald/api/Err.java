package com.herald.ezherald.api;

public enum Err {
    NOT_LOGIN(-1), IO_EXCEPTION(-2), PARAM_ERROR(400), UNAUTHORIZED(401), TIMEOUT(408), UNKNOWN(-3);

    private Err(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "" + code;
    }

    private int code;

    public static Err getErrFromHttpCode(int code) {
        for (Err e : Err.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return UNKNOWN;
    }


}
