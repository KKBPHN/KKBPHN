package com.airbnb.lottie;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.airbnb.lottie.O00000o.O00000o;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class O000o000 {
    public static Executor O00O00oo = Executors.newCachedThreadPool();
    private final Set O00O00o;
    private final Set O00O00oO;
    private final Handler handler;
    /* access modifiers changed from: private */
    @Nullable
    public volatile C0086O000Ooo result;

    @RestrictTo({Scope.LIBRARY})
    public O000o000(Callable callable) {
        this(callable, false);
    }

    @RestrictTo({Scope.LIBRARY})
    O000o000(Callable callable, boolean z) {
        this.O00O00o = new LinkedHashSet(1);
        this.O00O00oO = new LinkedHashSet(1);
        this.handler = new Handler(Looper.getMainLooper());
        this.result = null;
        if (z) {
            try {
                O000000o((C0086O000Ooo) callable.call());
            } catch (Throwable th) {
                O000000o(new C0086O000Ooo(th));
            }
        } else {
            O00O00oo.execute(new C0089O000Oooo(this, callable));
        }
    }

    /* access modifiers changed from: private */
    public void O000000o(@Nullable C0086O000Ooo o000Ooo) {
        if (this.result == null) {
            this.result = o000Ooo;
            O00OOoo();
            return;
        }
        throw new IllegalStateException("A task may only be set once.");
    }

    /* access modifiers changed from: private */
    public synchronized void O0000OOo(Object obj) {
        for (C0082O000OoO O000000o2 : new ArrayList(this.O00O00o)) {
            O000000o2.O000000o(obj);
        }
    }

    /* access modifiers changed from: private */
    public synchronized void O0000o0(Throwable th) {
        ArrayList<C0082O000OoO> arrayList = new ArrayList<>(this.O00O00oO);
        if (arrayList.isEmpty()) {
            O00000o.O00000Oo("Lottie encountered an error but no failure listener was added:", th);
            return;
        }
        for (C0082O000OoO O000000o2 : arrayList) {
            O000000o2.O000000o(th);
        }
    }

    private void O00OOoo() {
        this.handler.post(new C0088O000OooO(this));
    }

    public synchronized O000o000 O00000Oo(C0082O000OoO o000OoO) {
        if (!(this.result == null || this.result.getException() == null)) {
            o000OoO.O000000o(this.result.getException());
        }
        this.O00O00oO.add(o000OoO);
        return this;
    }

    public synchronized O000o000 O00000o(C0082O000OoO o000OoO) {
        this.O00O00oO.remove(o000OoO);
        return this;
    }

    public synchronized O000o000 O00000o0(C0082O000OoO o000OoO) {
        if (!(this.result == null || this.result.getValue() == null)) {
            o000OoO.O000000o(this.result.getValue());
        }
        this.O00O00o.add(o000OoO);
        return this;
    }

    public synchronized O000o000 O00000oO(C0082O000OoO o000OoO) {
        this.O00O00o.remove(o000OoO);
        return this;
    }
}
