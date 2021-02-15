package defpackage;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import com.google.lens.sdk.PendingIntentConsumer;
import java.util.ArrayDeque;
import java.util.Queue;

/* renamed from: aq reason: default package */
public final class aq implements ar {
    public final as a;
    public PendingIntentConsumer b;
    private final Queue c = new ArrayDeque();

    public aq(Context context, al alVar) {
        this.a = new ba(context, this, alVar);
    }

    private final boolean h() {
        z c2 = c();
        return (c2.a & 2) != 0 && this.a.b() >= c2.c;
    }

    public final void O000000o(ap apVar) {
        eb.c();
        if (this.a.c() || this.a.d()) {
            apVar.a(this.a.e());
            return;
        }
        ba baVar = (ba) this.a;
        if (!baVar.i() && !baVar.h()) {
            baVar.j();
        }
        this.c.add(apVar);
    }

    public final boolean O000000o(Bundle bundle) {
        eb.c();
        if (!this.a.c()) {
            return false;
        }
        n nVar = (n) o.c.e();
        int i = m.aU;
        if (nVar.c) {
            nVar.b();
            nVar.c = false;
        }
        o oVar = (o) nVar.b;
        int i2 = i - 1;
        if (i != 0) {
            oVar.b = i2;
            oVar.a |= 1;
            o oVar2 = (o) nVar.h();
            try {
                this.a.O00000Oo(oVar2.m(), new k(bundle));
                return true;
            } catch (RemoteException | SecurityException e) {
                Log.e("LensServiceBridge", "Failed to inject image.", e);
                return false;
            }
        } else {
            throw null;
        }
    }

    public final void a() {
        eb.c();
        ((ba) this.a).j();
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [as, android.content.ServiceConnection] */
    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r2v2 */
    /* JADX WARNING: type inference failed for: r6v3 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v0, types: [as, android.content.ServiceConnection]
  assigns: [as]
  uses: [?[OBJECT, ARRAY], android.content.ServiceConnection]
  mth insns count: 56
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void b() {
        eb.c();
        ? r0 = this.a;
        eb.c();
        ba baVar = (ba) r0;
        String str = "LensServiceConnImpl";
        if (baVar.g()) {
            n nVar = (n) o.c.e();
            int i = m.cH;
            if (nVar.c) {
                nVar.b();
                nVar.c = false;
            }
            o oVar = (o) nVar.b;
            int i2 = i - 1;
            if (i != 0) {
                oVar.b = i2;
                oVar.a |= 1;
                o oVar2 = (o) nVar.h();
                try {
                    g gVar = ((ba) r0).g;
                    eb.O00000oO(gVar);
                    gVar.a(oVar2.m());
                } catch (RemoteException | SecurityException e) {
                    Log.e(str, "Unable to end Lens service session.", e);
                }
                baVar.g = null;
                baVar.c = 0;
                baVar.d = null;
                baVar.e = null;
                int i3 = bh.a;
            } else {
                throw null;
            }
        }
        if (baVar.h()) {
            try {
                ((ba) r0).b.unbindService(r0);
            } catch (IllegalArgumentException unused) {
                Log.w(str, "Unable to unbind, service is not registered.");
            }
            baVar.f = null;
        }
        baVar.h = bh.a;
        baVar.a(1);
        this.b = null;
    }

    public final z c() {
        eb.c();
        eb.a(this.a.c(), "getServerFlags() called before ready.");
        if (!this.a.c()) {
            return z.f;
        }
        as asVar = this.a;
        eb.c();
        ba baVar = (ba) asVar;
        eb.a(baVar.g(), "Attempted to use ServerFlags before ready.");
        return baVar.d;
    }

    public final x d() {
        eb.c();
        eb.a(this.a.c(), "getLensCapabilities() called when Lens is not ready.");
        if (!this.a.c()) {
            return x.b;
        }
        as asVar = this.a;
        eb.c();
        ba baVar = (ba) asVar;
        eb.a(baVar.g(), "Attempted to use LensCapabilities before ready.");
        return baVar.e;
    }

    public final void e() {
        while (this.c.peek() != null) {
            ((ap) this.c.remove()).a(this.a.e());
        }
    }

    public final int f() {
        eb.c();
        if (!this.a.c()) {
            return this.a.e();
        }
        if (h()) {
            z c2 = c();
            if ((c2.a & 8) != 0 && this.a.b() >= c2.e) {
                return bh.b;
            }
        }
        return bh.l;
    }

    public final int g() {
        eb.c();
        return !this.a.c() ? this.a.e() : h() ? bh.b : bh.l;
    }
}
