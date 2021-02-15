package defpackage;

import android.os.RemoteException;
import android.util.Log;

/* renamed from: av reason: default package */
final /* synthetic */ class av implements Runnable {
    private final ba a;
    private final e b;

    public av(ba baVar, e eVar) {
        this.a = baVar;
        this.b = eVar;
    }

    public final void run() {
        ba baVar = this.a;
        try {
            baVar.a.execute(new ax(baVar, this.b.O000000o("LENS_SERVICE_SESSION", baVar)));
        } catch (RemoteException e) {
            Log.w("LensServiceConnImpl", "Failed to create a Lens service session.", e);
            baVar.a.execute(new ay(baVar));
        }
    }
}
