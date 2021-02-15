package defpackage;

import java.util.concurrent.ThreadFactory;

/* renamed from: at reason: default package */
final /* synthetic */ class at implements ThreadFactory {
    static final ThreadFactory a = new at();

    private at() {
    }

    public final Thread newThread(Runnable runnable) {
        return new Thread(runnable, "LensSvConn");
    }
}
