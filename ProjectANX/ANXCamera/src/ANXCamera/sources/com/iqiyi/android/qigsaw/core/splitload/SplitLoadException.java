package com.iqiyi.android.qigsaw.core.splitload;

final class SplitLoadException extends Exception {
    private final int errorCode;

    SplitLoadException(int i, Throwable th) {
        StringBuilder sb = new StringBuilder(32);
        sb.append("Split Load Error: ");
        sb.append(i);
        super(sb.toString(), th);
        this.errorCode = i;
    }

    /* access modifiers changed from: 0000 */
    public int getErrorCode() {
        return this.errorCode;
    }
}
