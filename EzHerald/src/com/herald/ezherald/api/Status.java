package com.herald.ezherald.api;

public enum Status {
    SUCCESS(0), UNKNOWN(-3),
    NOT_LOGIN(-1), IO_EXCEPTION(-2),PARAM_ERROR(400), UNAUTHORIZED(401), TIMEOUT(408);

    private Status(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "" + code;
    }

    private int code;
    public int getCode(){
        return code;
    }

    public static Status getErrFromHttpCode(int code) {
        for (Status e : Status.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return UNKNOWN;
    }
}
