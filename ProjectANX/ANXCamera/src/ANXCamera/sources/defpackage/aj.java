package defpackage;

import android.os.AsyncTask.Status;

/* renamed from: aj reason: default package */
final class aj implements Runnable {
    final /* synthetic */ ak a;

    public aj(ak akVar) {
        this.a = akVar;
    }

    public final void run() {
        if (this.a.getStatus() != Status.FINISHED) {
            this.a.cancel(true);
            ak akVar = this.a;
            int i = bh.f;
            akVar.a(i, i);
        }
    }
}
