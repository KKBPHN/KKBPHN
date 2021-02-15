package defpackage;

import android.os.Handler;
import java.util.concurrent.Executor;

/* renamed from: az reason: default package */
final class az implements Executor {
    private final Handler a;

    public az(Handler handler) {
        this.a = handler;
    }

    public final void execute(Runnable runnable) {
        this.a.post(runnable);
    }
}
