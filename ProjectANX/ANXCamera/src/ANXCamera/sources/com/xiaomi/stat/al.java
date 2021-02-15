package com.xiaomi.stat;

import java.lang.Thread.UncaughtExceptionHandler;

public class al implements UncaughtExceptionHandler {
    private e a;
    private UncaughtExceptionHandler b;
    private boolean c = true;

    public al(e eVar) {
        this.a = eVar;
    }

    public void a() {
        UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!(defaultUncaughtExceptionHandler instanceof al)) {
            this.b = defaultUncaughtExceptionHandler;
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    public void a(boolean z) {
        this.c = z;
    }

    public void uncaughtException(Thread thread, Throwable th) {
        if (this.c) {
            this.a.a(th, (String) null, false);
        }
        UncaughtExceptionHandler uncaughtExceptionHandler = this.b;
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, th);
        }
    }
}
