package com.herald.ezherald.api;

public interface FailHandler {
    public void onFail(Status status, String message);
}
