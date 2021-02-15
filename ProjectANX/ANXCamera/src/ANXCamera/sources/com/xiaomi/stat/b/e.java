package com.xiaomi.stat.b;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class e {

    class a {
        /* access modifiers changed from: private */
        public static final ExecutorService a = Executors.newCachedThreadPool();

        private a() {
        }
    }

    private e() {
    }

    public static ExecutorService a() {
        return a.a;
    }
}
