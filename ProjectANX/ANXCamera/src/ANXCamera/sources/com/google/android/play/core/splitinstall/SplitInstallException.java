package com.google.android.play.core.splitinstall;

public class SplitInstallException extends RuntimeException {
    private final int errorCode;

    SplitInstallException(int i) {
        StringBuilder sb = new StringBuilder(32);
        sb.append("Split Install Error: ");
        sb.append(i);
        super(sb.toString());
        this.errorCode = i;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
