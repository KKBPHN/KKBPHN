package com.xiaomi.idm.api;

public class RmiException extends Exception {
    private int mResponseCode;

    public RmiException(int i) {
        this(i, ResponseCode.getResponseMsg(i));
    }

    public RmiException(int i, String str) {
        super(str);
        this.mResponseCode = i;
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getMessage());
        sb.append("; response code: ");
        sb.append(this.mResponseCode);
        return sb.toString();
    }

    public int getResponseCode() {
        return this.mResponseCode;
    }
}
